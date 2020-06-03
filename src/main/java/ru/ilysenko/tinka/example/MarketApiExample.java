/*
 * The GNU Affero General Public License v3.0 (AGPL-3.0)
 *
 * Copyright (c) 2020 Ilya Lysenko
 *
 * Permissions of this strongest copyleft license are conditioned on making available complete source code of licensed
 * works and modifications, which include larger works using a licensed work, under the same license.
 * Copyright and license notices must be preserved. Contributors provide an express grant of patent rights.
 * When a modified version is used to provide a service over a network,  the complete source code of the modified
 * version must be made available.
 */
package ru.ilysenko.tinka.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.threeten.bp.OffsetDateTime;
import ru.ilysenko.tinka.helper.MarketApiHelper;
import ru.ilysenko.tinka.indicator.*;
import ru.ilysenko.tinka.model.Ticker;
import ru.tinkoff.invest.model.*;

import java.util.List;

import static java.lang.String.format;
import static ru.ilysenko.tinka.helper.CalculationHelper.differenceRate2String;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class MarketApiExample {
    private final MarketApiHelper marketApiHelper;

    @EventListener(ApplicationReadyEvent.class)
    public void execute() {
        example1();
        example2();
        example3();
        shutdown();
    }

    private void example1() {
        log.info("");
        log.info("===Example 1===");
        log.info("");

        Ticker ticker = Ticker.TESLA;
        MarketInstrument marketInstrument = getMarketInstrument(ticker);
        List<Candle> candles = getCandles(marketInstrument.getFigi());

        if (candles.isEmpty()) {
            log.warn("Candles for {} is not found", ticker);
        } else {
            Candle currentCandle = candles.get(0);
            Candle previousCandle = candles.size() > 1 ? candles.get(1) : currentCandle;
            double price = currentCandle.getC();
            double previousPrice = previousCandle.getC();
            int volume = currentCandle.getV();
            int previousVolume = previousCandle.getV();
            int lotSize = marketInstrument.getLot();
            double moneyVolume = volume * price * lotSize;
            double previousMoneyVolume = previousVolume * previousPrice * lotSize;

            log.info("Ticker: {}", ticker.getValue());
            log.info("Name: {}", marketInstrument.getName());
            log.info("Prev price: {}", previousPrice);
            log.info("Open price: {} {}", currentCandle.getO(), differenceRate2String(previousPrice, currentCandle.getO()));
            log.info("Current price: {} {}", price, differenceRate2String(previousPrice, price));
            log.info("Highest price: {} {}", currentCandle.getH(), differenceRate2String(previousPrice, currentCandle.getH()));
            log.info("Lowest price: {} {}", currentCandle.getL(), differenceRate2String(previousPrice, currentCandle.getL()));
            log.info("Trade volume: {} {}", volume, differenceRate2String(previousVolume, volume));
            log.info("Money volume: {} {} {}", marketInstrument.getCurrency(), format("%.0f", moneyVolume), differenceRate2String(previousMoneyVolume, moneyVolume));
        }
    }

    private void example2() {
        log.info("");
        log.info("===Example 2===");
        log.info("");

        Ticker ticker = Ticker.BOEING;
        List<Candle> candles = getCandles(ticker);

        if (candles.isEmpty()) {
            log.warn("Candles for {} is not found", ticker);
        } else {
            Indicator rsiIndicator = RsiIndicator.create().periodsCount(14).init();
            Indicator cciIndicator = CciIndicator.create().periodsCount(14).init();
            Indicator williamsRIndicator = WilliamsRIndicator.create().periodsCount(14).init();
            Indicator dpoIndicator = DpoIndicator.create().periodsCount(14).init();
            Indicator momentumIndicator = MomentumIndicator.create().periodsCount(14).init();

            log.info("Ticker: {}", ticker.getValue());
            log.info("Name: {}", getMarketInstrument(ticker).getName());
            log.info("RSI indicator: {} ({})", format("%.2f", rsiIndicator.calculate(candles)), rsiIndicator.getStateName(candles));
            log.info("CCI indicator: {} ({})", format("%.2f", cciIndicator.calculate(candles)), cciIndicator.getStateName(candles));
            log.info("Williams %R indicator: {} ({})", format("%.2f", williamsRIndicator.calculate(candles)), williamsRIndicator.getStateName(candles));
            log.info("DPO indicator: {} ({})", format("%.2f", dpoIndicator.calculate(candles)), dpoIndicator.getStateName(candles));
            log.info("Momentum indicator: {} ({})", format("%.2f", momentumIndicator.calculate(candles)), momentumIndicator.getStateName(candles));
        }
    }

    private void example3() {
        log.info("");
        log.info("===Example 3===");
        log.info("");

        Ticker ticker = Ticker.FACEBOOK;
        MarketInstrument marketInstrument = getMarketInstrument(ticker);
        List<Candle> candles = getCandles(marketInstrument.getFigi());

        if (candles.isEmpty()) {
            log.warn("Candles for {} is not found", ticker);
        } else {
            Candle currentCandle = candles.get(0);
            double price = currentCandle.getC();
            int orderBookDepth = 6;

            Orderbook payload = marketApiHelper.getOrderBook(ticker, orderBookDepth);
            int bidsCount = payload.getBids().stream().mapToInt(OrderResponse::getQuantity).sum();
            int asksCount = payload.getAsks().stream().mapToInt(OrderResponse::getQuantity).sum();

            log.info("Current price: {}   bids {} | asks {}", price, bidsCount, asksCount);
        }
    }

    private MarketInstrument getMarketInstrument(Ticker ticker) {
        return marketApiHelper.getInstrument(ticker);
    }

    private List<Candle> getCandles(Ticker ticker) {
        String figi = marketApiHelper.getFigi(ticker);
        return getCandles(figi);
    }

    private List<Candle> getCandles(String figi) {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime from = now.minusMonths(2);
        OffsetDateTime to = now;
        CandleResolution candleResolution = CandleResolution.DAY;

        return marketApiHelper.getCandles(figi, from, to, candleResolution);
    }

    private void shutdown() {
        log.info("");
        System.exit(0);
    }
}

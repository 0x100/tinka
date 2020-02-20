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
import ru.ilysenko.tinka.model.Ticker;
import ru.tinkoff.invest.model.Candle;
import ru.tinkoff.invest.model.CandleResolution;

import java.util.List;

import static ru.ilysenko.tinka.helper.CalcHelper.differenceRate2String;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class MarketApiExample {
    private final MarketApiHelper marketApiHelper;

    @EventListener(ApplicationReadyEvent.class)
    public void getCurrentPrice() {
        Ticker ticker = Ticker.TESLA;

        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime from = now.minusWeeks(1);
        OffsetDateTime to = now;
        String figi = marketApiHelper.getFigi(ticker);
        CandleResolution candleResolution = CandleResolution.DAY;

        List<Candle> candles = marketApiHelper.getCandles(figi, from, to, candleResolution);
        if (candles.isEmpty()) {
            log.warn("Candles for {} is not found", ticker);
        } else {
            Candle currentCandle = candles.get(0);
            Candle previousCandle = candles.size() > 1 ? candles.get(1) : currentCandle;
            Double currentPrice = currentCandle.getC();

            log.info("\n\n");
            log.info("Ticker: {}", ticker.getValue());
            log.info("Prev price: {}", previousCandle.getC());
            log.info("Open price: {} {}", currentCandle.getO(), differenceRate2String(previousCandle.getC(), currentCandle.getO()));
            log.info("Current price: {} {}", currentPrice, differenceRate2String(previousCandle.getC(), currentPrice));
            log.info("Highest price: {} {}", currentCandle.getH(), differenceRate2String(currentPrice, currentCandle.getH()));
            log.info("Lowest price: {} {}", currentCandle.getL(), differenceRate2String(currentPrice, currentCandle.getL()));
            log.info("Volume: {} {}", currentCandle.getV(), differenceRate2String(previousCandle.getV(), currentCandle.getV()));
        }
    }
}

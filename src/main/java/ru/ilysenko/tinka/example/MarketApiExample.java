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
import ru.tinkoff.invest.api.MarketApi;
import ru.tinkoff.invest.model.CandleResolution;
import ru.tinkoff.invest.model.Candles;
import ru.tinkoff.invest.model.CandlesResponse;

import java.util.Optional;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class MarketApiExample {

    private final MarketApi marketApi;
    private final MarketApiHelper marketApiHelper;

    @EventListener(ApplicationReadyEvent.class)
    public void getCurrentPrice() {
        Ticker ticker = Ticker.GOOGLE;

        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime from = now.minusWeeks(1);
        OffsetDateTime to = now;
        String figi = marketApiHelper.getFigi(ticker);
        CandleResolution candleResolution = CandleResolution.WEEK;

        CandlesResponse response = marketApi.marketCandlesGet(figi, from, to, candleResolution);
        Optional.ofNullable(response)
                .map(CandlesResponse::getPayload)
                .map(Candles::getCandles)
                .flatMap(c -> c.stream().findFirst())
                .ifPresentOrElse(
                        candle -> {
                            log.info("Ticker: {}", ticker.getValue());
                            log.info("Open price: {}", candle.getO());
                            log.info("Current price: {}", candle.getC());
                            log.info("Highest price: {}", candle.getH());
                            log.info("Lowest price: {}", candle.getL());
                        },
                        () -> log.warn("Today's candle for {} is not found", ticker));
    }
}

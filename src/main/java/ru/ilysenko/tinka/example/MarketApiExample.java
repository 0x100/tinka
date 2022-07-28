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
import ru.ilysenko.tinka.mapper.CandleMapper;
import ru.ilysenko.tinka.tools.strategy.HeikinAshiStrategy;
import ru.tinkoff.invest.api.InstrumentsServiceApi;
import ru.tinkoff.invest.api.MarketDataServiceApi;
import ru.tinkoff.invest.api.OperationsServiceApi;
import ru.tinkoff.invest.api.SandboxServiceApi;
import ru.tinkoff.invest.api.UsersServiceApi;
import ru.tinkoff.invest.model.V1CandleInterval;
import ru.tinkoff.invest.model.V1GetCandlesRequest;
import ru.tinkoff.invest.model.V1HistoricCandle;

import java.util.List;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class MarketApiExample {
    private static final long SECOND = 1000L;
    public static final int WINDOW_SIZE = HeikinAshiStrategy.MIN_CANDLES_SIZE;

    private final MarketApiHelper marketApiHelper;
    private final SandboxServiceApi sandboxServiceApi;
    private final HeikinAshiStrategy heikinAshiStrategy;
    private final UsersServiceApi userApi;
    private final OperationsServiceApi operationsApi;
    private final InstrumentsServiceApi instrumentsServiceApi;
    private final MarketDataServiceApi marketDataServiceApi;

    private final CandleMapper candleMapper;

    @EventListener(ApplicationReadyEvent.class)
    public void execute() {

        List<V1HistoricCandle> candles = getCandles();

        shutdown();
    }

    private List<V1HistoricCandle> getCandles() {
        OffsetDateTime from = OffsetDateTime.parse("2022-07-14T23:18:40Z");
        OffsetDateTime to = OffsetDateTime.parse("2022-07-15T01:24:40Z");
        V1GetCandlesRequest request = new V1GetCandlesRequest();
        request.setFrom(from);
        request.setTo(to);
        request.setFigi("BBG000BBQCY0");
        request.setInterval(V1CandleInterval._1_MIN);
        return marketDataServiceApi.marketDataServiceGetCandles(request).getCandles();
    }

    private void shutdown() {
        log.info("");
        System.exit(0);
    }

//    @Scheduled(fixedDelay = 30 * SECOND)
//    public void example4() {
//        List<V1HistoricCandle> candles = getCandles();
//
//        TradingAdvice advice = heikinAshiStrategy.solve(candles);
//        log.info("Advide: {}", advice);
//    }
}

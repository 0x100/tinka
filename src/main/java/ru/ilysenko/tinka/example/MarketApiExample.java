package ru.ilysenko.tinka.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.threeten.bp.LocalTime;
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
@RequiredArgsConstructor
public class MarketApiExample {

    private final MarketApi marketApi;
    private final MarketApiHelper marketApiHelper;

    @EventListener(ApplicationReadyEvent.class)
    public void getCurrentPrice() {
        Ticker ticker = Ticker.SBERBANK;

        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime from = now.with(LocalTime.MIN);
        OffsetDateTime to = now;
        String figi = marketApiHelper.getFigi(ticker);
        CandleResolution candleResolution = CandleResolution.DAY;

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

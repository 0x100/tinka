package ru.ilysenko.tinka;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.threeten.bp.OffsetDateTime;
import ru.ilysenko.tinka.model.Ticker;
import ru.tinkoff.invest.api.MarketApi;
import ru.tinkoff.invest.model.Candle;
import ru.tinkoff.invest.model.CandleResolution;
import ru.tinkoff.invest.model.Candles;
import ru.tinkoff.invest.model.CandlesResponse;
import ru.tinkoff.invest.model.MarketInstrument;
import ru.tinkoff.invest.model.MarketInstrumentList;
import ru.tinkoff.invest.model.MarketInstrumentListResponse;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApiClientTest {

    @Autowired
    private MarketApi marketApi;

    @Test
    public void getFigi() {
        String ticker = Ticker.SBERBANK.getValue();
        String figi = getFigi(ticker);

        assertNotNull(figi);
        log.info("Figi for the ticker " + ticker + " is " + figi);
    }

    @Test
    public void getCandles() {
        String figi = getFigi(Ticker.APPLE.getValue());
        CandlesResponse response = marketApi.marketCandlesGet(figi, OffsetDateTime.now().minusDays(10), OffsetDateTime.now(), CandleResolution.DAY);
        assertNotNull(response);

        Candles payload = response.getPayload();
        assertNotNull(payload);

        List<Candle> candles = payload.getCandles();
        assertNotNull(candles);
        assertFalse(candles.isEmpty());
    }

    private String getFigi(String ticker) {
        List<MarketInstrument> instruments = ofNullable(marketApi.marketSearchByTickerGet(ticker))
                .map(MarketInstrumentListResponse::getPayload)
                .map(MarketInstrumentList::getInstruments)
                .orElse(Collections.emptyList());

        return instruments.stream()
                .filter(instrument -> Objects.equals(ticker, instrument.getTicker()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ticker not found"))
                .getFigi();
    }
}

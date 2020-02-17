package ru.ilysenko.tinka;

import io.swagger.client.api.MarketApi;
import io.swagger.client.model.CandleResolution;
import io.swagger.client.model.CandlesResponse;
import io.swagger.client.model.MarketInstrument;
import io.swagger.client.model.MarketInstrumentList;
import io.swagger.client.model.MarketInstrumentListResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.threeten.bp.OffsetDateTime;
import ru.ilysenko.tinka.model.Ticker;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;
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
    @Ignore
    public void getCandles() {
        String figi = getFigi(Ticker.APPLE.getValue());
        CandlesResponse response = marketApi.marketCandlesGet(figi, OffsetDateTime.now().minusDays(10), OffsetDateTime.now(), CandleResolution.DAY);
        assertNotNull(response);
        log.info("payload: " + response.getPayload());
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

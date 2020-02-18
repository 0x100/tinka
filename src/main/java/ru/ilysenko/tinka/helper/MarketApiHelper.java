package ru.ilysenko.tinka.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ilysenko.tinka.model.Ticker;
import ru.tinkoff.invest.api.MarketApi;
import ru.tinkoff.invest.model.MarketInstrument;
import ru.tinkoff.invest.model.MarketInstrumentList;
import ru.tinkoff.invest.model.MarketInstrumentListResponse;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class MarketApiHelper {

    private final MarketApi marketApi;

    public String getFigi(Ticker ticker) {
        return getMarketInstruments(ticker.getValue()).stream()
                .filter(instrument -> Objects.equals(ticker.getValue(), instrument.getTicker()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ticker not found"))
                .getFigi();
    }

    private List<MarketInstrument> getMarketInstruments(String ticker) {
        return ofNullable(marketApi.marketSearchByTickerGet(ticker))
                .map(MarketInstrumentListResponse::getPayload)
                .map(MarketInstrumentList::getInstruments)
                .orElse(Collections.emptyList());
    }
}

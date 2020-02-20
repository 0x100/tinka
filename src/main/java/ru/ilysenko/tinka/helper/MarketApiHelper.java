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
package ru.ilysenko.tinka.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class MarketApiHelper {

    private final MarketApi marketApi;

    /**
     * Get FIGI (instrument identifier) for a specified ticker
     *
     * @param ticker ticker value (for example, AAPL for Apple)
     * @return instrument id
     */
    public String getFigi(Ticker ticker) {
        return getMarketInstruments(ticker.getValue()).stream()
                .filter(instrument -> Objects.equals(ticker.getValue(), instrument.getTicker()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ticker not found"))
                .getFigi();
    }

    /**
     * Get candles in descending order
     *
     * @param figi             instrument id
     * @param from             from date
     * @param to               due date
     * @param candleResolution candle period
     * @return list of candles
     */
    public List<Candle> getCandles(String figi, OffsetDateTime from, OffsetDateTime to, CandleResolution candleResolution) {
        return getCandles(figi, from, to, candleResolution, false);
    }

    /**
     * Get candles
     *
     * @param figi             instrument id
     * @param from             from date
     * @param to               due date
     * @param candleResolution candle period
     * @return list of candles
     */
    public List<Candle> getCandles(String figi, OffsetDateTime from, OffsetDateTime to, CandleResolution candleResolution, boolean ascendingOrder) {
        CandlesResponse response = marketApi.marketCandlesGet(figi, from, to, candleResolution);
        return of(response)
                .map(CandlesResponse::getPayload)
                .map(Candles::getCandles)
                .orElse(Collections.emptyList())
                .stream()
                .sorted((c1, c2) -> ascendingOrder ? c1.getTime().compareTo(c2.getTime()) : c2.getTime().compareTo(c1.getTime()))
                .collect(Collectors.toList());
    }

    private List<MarketInstrument> getMarketInstruments(String ticker) {
        return ofNullable(marketApi.marketSearchByTickerGet(ticker))
                .map(MarketInstrumentListResponse::getPayload)
                .map(MarketInstrumentList::getInstruments)
                .orElse(Collections.emptyList());
    }
}

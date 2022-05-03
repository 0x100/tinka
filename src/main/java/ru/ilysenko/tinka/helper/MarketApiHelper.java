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
import ru.tinkoff.invest.api.InstrumentsServiceApi;
import ru.tinkoff.invest.api.MarketDataServiceApi;
import ru.tinkoff.invest.model.V1CandleInterval;
import ru.tinkoff.invest.model.V1GetCandlesRequest;
import ru.tinkoff.invest.model.V1GetCandlesResponse;
import ru.tinkoff.invest.model.V1GetOrderBookRequest;
import ru.tinkoff.invest.model.V1GetOrderBookResponse;
import ru.tinkoff.invest.model.V1HistoricCandle;
import ru.tinkoff.invest.model.V1Instrument;
import ru.tinkoff.invest.model.V1InstrumentIdType;
import ru.tinkoff.invest.model.V1InstrumentRequest;
import ru.tinkoff.invest.model.V1InstrumentResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class MarketApiHelper {

    private final MarketDataServiceApi marketApi;
    private final InstrumentsServiceApi instrumentApi;

    /**
     * Get FIGI (instrument identifier) for a specified ticker
     *
     * @param ticker ticker value (for example, AAPL for Apple)
     * @return instrument id
     */
    public String getFigi(Ticker ticker) {
        return getInstrument(ticker).getFigi();
    }

    /**
     * Get market instrument info: figi, ticker, isin, minPriceIncrement, lot size, currency, name, type
     *
     * @param ticker {@link Ticker} value
     * @return market instrument info
     */
    public V1Instrument getInstrument(Ticker ticker) {
        V1InstrumentRequest request = new V1InstrumentRequest()
                .idType(V1InstrumentIdType.TYPE_TICKER)
                .id(ticker.getValue())
                .classCode(ticker.getClassCode().name());

        return ofNullable(instrumentApi.instrumentsServiceGetInstrumentBy(request))
                .map(V1InstrumentResponse::getInstrument)
                .orElseThrow(() -> new RuntimeException("Ticker not found"));
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
    public List<V1HistoricCandle> getCandles(String figi, OffsetDateTime from, OffsetDateTime to, V1CandleInterval candleResolution) {
        return getCandles(figi, from, to, candleResolution, false);
    }

    /**
     * Get candles
     *
     * @param figi     instrument id
     * @param from     from date
     * @param to       due date
     * @param interval candle period
     * @return list of candles
     */
    public List<V1HistoricCandle> getCandles(String figi, OffsetDateTime from, OffsetDateTime to, V1CandleInterval interval, boolean ascendingOrder) {
        V1GetCandlesRequest request = new V1GetCandlesRequest().figi(figi).from(from).to(to).interval(interval);
        V1GetCandlesResponse response = marketApi.marketDataServiceGetCandles(request);
        return of(response)
                .map(V1GetCandlesResponse::getCandles)
                .orElse(Collections.emptyList())
                .stream()
                .sorted((c1, c2) -> ascendingOrder ? c1.getTime().compareTo(c2.getTime()) : c2.getTime().compareTo(c1.getTime()))
                .collect(Collectors.toList());
    }

    /**
     * Get candles
     *
     * @param ticker   ticker
     * @param from     from date
     * @param to       due date
     * @param interval candle period
     * @return list of candles
     */
    public List<V1HistoricCandle> getCandles(Ticker ticker, OffsetDateTime from, OffsetDateTime to, V1CandleInterval interval) {
        V1GetCandlesRequest request = new V1GetCandlesRequest();
        request.setFigi(getFigi(ticker));
        request.setFrom(from);
        request.setTo(to);
        request.setInterval(interval);
        V1GetCandlesResponse response = marketApi.marketDataServiceGetCandles(request);
        return response.getCandles();
    }

    public V1GetOrderBookResponse getOrderBook(Ticker ticker, int depth) {
        V1GetOrderBookRequest request = new V1GetOrderBookRequest();
        request.setFigi(getFigi(ticker));
        request.setDepth(depth);
        return marketApi.marketDataServiceGetOrderBook(request);
    }
}

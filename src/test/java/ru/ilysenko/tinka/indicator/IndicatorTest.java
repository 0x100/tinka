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
package ru.ilysenko.tinka.indicator;

import ru.tinkoff.invest.model.Candle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

abstract class IndicatorTest {

    abstract Indicator makeIndicator();

    List<Candle> getCandles() {
        List<Candle> candles = new ArrayList<>();
        /*
         * First candles of the NET (Cloudflare) ticker at the month time frame (from 2019-09-02 to 2020-01-01)
         */
        candles.add(makeCandle(17.82, 19.3, 16.8));
        candles.add(makeCandle(17.04, 19.4, 16.24));
        candles.add(makeCandle(19.47, 19.8, 15.55));
        candles.add(makeCandle(16.83, 18.66, 14.5));
        candles.add(makeCandle(18.58, 22.07, 17.54));
        return candles;
    }

    void testOverboughtState(double threshold) {
        Indicator indicator = makeIndicator();
        when(indicator.calculate(anyList())).thenReturn(threshold);
        assertEquals(IndicatorState.OVERBOUGHT, indicator.getState(Collections.emptyList()));
    }

    void testOversoldState(double threshold) {
        Indicator indicator = makeIndicator();
        when(indicator.calculate(anyList())).thenReturn(threshold);
        assertEquals(IndicatorState.OVERSOLD, indicator.getState(Collections.emptyList()));
    }

    private Candle makeCandle(double closePrice, double highestPrice, double lowestPrice) {
        Candle candle = new Candle();
        candle.setC(closePrice);
        candle.setH(highestPrice);
        candle.setL(lowestPrice);

        return candle;
    }

    String format(double value) {
        return String.format(Locale.ROOT, "%.4f", value);
    }
}

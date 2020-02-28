package ru.ilysenko.tinka.indicator;

import ru.tinkoff.invest.model.Candle;

import java.util.ArrayList;
import java.util.List;

abstract class IndicatorTest {

    List<Candle> getCandles() {
        List<Candle> candles = new ArrayList<>();
        /*
         * First candles of the NET ticker on the Month time-frame (from 2019-09-02 to 2020-01-01)
         */
        candles.add(makeCandle(17.82, 19.3, 16.8));
        candles.add(makeCandle(17.04, 19.4, 16.24));
        candles.add(makeCandle(19.47, 19.8, 15.55));
        candles.add(makeCandle(16.83, 18.66, 14.5));
        candles.add(makeCandle(18.58, 22.07, 17.54));
        return candles;
    }

    private Candle makeCandle(double closePrice, double highestPrice, double lowestPrice) {
        Candle candle = new Candle();
        candle.setC(closePrice);
        candle.setH(highestPrice);
        candle.setL(lowestPrice);

        return candle;
    }
}

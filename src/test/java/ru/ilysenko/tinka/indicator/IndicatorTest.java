package ru.ilysenko.tinka.indicator;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.tinkoff.invest.model.Candle;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public abstract class IndicatorTest {

    List<Candle> getCandles() {
        List<Candle> candles = new ArrayList<>();
        candles.add(makeCandle(60.12, 60.14, 59.43));
        candles.add(makeCandle(59.72, 59.86, 59.11));
        candles.add(makeCandle(59.77, 60.00, 59.47));
        candles.add(makeCandle(59.53, 59.93, 59.45));
        candles.add(makeCandle(59.95, 60.07, 59.62));
        return candles;
    }

    private Candle makeCandle(double closePrice, double highestPrice, double lowestPrice) {
        Candle candle = new Candle();
        candle.setC(closePrice);
        candle.setL(lowestPrice);
        candle.setH(highestPrice);

        return candle;
    }
}

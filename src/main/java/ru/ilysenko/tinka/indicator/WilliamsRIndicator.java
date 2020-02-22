package ru.ilysenko.tinka.indicator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ru.tinkoff.invest.model.Candle;

import java.util.List;
import java.util.function.Function;

/**
 * Implementation of the Williams %R indicator
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WilliamsRIndicator implements Indicator {
    private int length = 3;

    @Override
    public double calculate(List<Candle> candles) {
        if(candles.size() < length) {
            return Double.NaN;
        }
        double maxHi = getMaxHi(candles);
        double minLo = getMinLo(candles);
        Double currentPrice = getLastCandle(candles).getC();

        return ((currentPrice - maxHi) / (maxHi - minLo)) * 100;
    }

    private double getMinLo(List<Candle> candles) {
        return candles.stream()
                .limit(length)
                .map(Candle::getL)
                .min(Double::compare)
                .orElseThrow();
    }

    private double getMaxHi(List<Candle> candles) {
        return candles.stream()
                .limit(length)
                .map(Candle::getH)
                .max(Double::compare)
                .orElseThrow();
    }

    private Candle getLastCandle(List<Candle> candles) {
        return candles.get(0);
    }
}

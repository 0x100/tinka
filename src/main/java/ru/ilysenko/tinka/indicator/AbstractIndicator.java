package ru.ilysenko.tinka.indicator;

import org.springframework.util.Assert;
import ru.tinkoff.invest.model.Candle;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractIndicator implements Indicator {

    abstract int getOverboughtThreshold();

    abstract int getOversoldThreshold();

    @Override
    public boolean isOverbought(List<Candle> candles) {
        return calculate(candles) > getOverboughtThreshold();
    }

    @Override
    public boolean isOversold(List<Candle> candles) {
        return calculate(candles) < getOversoldThreshold();
    }

    @Override
    public IndicatorState getState(List<Candle> candles) {
        if (isOverbought(candles)) {
            return IndicatorState.OVERBOUGHT;
        } else if (isOversold(candles)) {
            return IndicatorState.OVERSOLD;
        }
        return IndicatorState.NEUTRAL;
    }

    @Override
    public String getStateName(List<Candle> candles) {
        IndicatorState state = getState(candles);
        return state.name().toLowerCase();
    }

    protected void validate(List<Candle> candles) {
        Assert.isTrue(candles.get(0).getTime().isAfter(candles.get(1).getTime()), "Wrong dates order");
    }

    List<Candle> limitCandles(List<Candle> candles, int periodsCount) {
        return candles.stream()
                .limit(periodsCount)
                .collect(Collectors.toList());
    }
}

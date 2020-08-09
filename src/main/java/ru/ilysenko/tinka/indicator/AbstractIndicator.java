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

import org.springframework.util.Assert;
import ru.tinkoff.invest.model.Candle;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractIndicator implements Indicator {

    abstract double getOverboughtThreshold();

    abstract double getOversoldThreshold();

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

    List<Candle> limitCandles(List<Candle> candles, int count) {
        return candles.stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    List<Candle> skipCandles(List<Candle> candles, int count) {
        return candles.stream()
                .skip(count)
                .collect(Collectors.toList());
    }
}

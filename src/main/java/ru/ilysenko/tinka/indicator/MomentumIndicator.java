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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ru.tinkoff.invest.model.Candle;

import java.util.List;

/**
 * Implementation of the Momentum indicator
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "create", buildMethodName = "init")
public class MomentumIndicator extends AbstractIndicator {
    private int periodsCount = 14;

    private static final int OVERBOUGHT_THRESHOLD = 100;
    private static final int OVERSOLD_THRESHOLD = 100;

    @Override
    public double calculate(List<Candle> candles) {
        candles = limitCandles(candles, periodsCount + 1);

        if (candles.size() < periodsCount + 1) {
            return Double.NaN;
        }
        validate(candles);

        double currentPrice = candles.get(0).getC();
        double previousPrice = candles.get(periodsCount).getC();

        return currentPrice - previousPrice;
    }

    @Override
    public int getOverboughtThreshold() {
        return OVERBOUGHT_THRESHOLD;
    }

    @Override
    public int getOversoldThreshold() {
        return OVERSOLD_THRESHOLD;
    }
}

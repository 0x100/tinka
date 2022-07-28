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
package ru.ilysenko.tinka.tools.indicator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ru.tinkoff.invest.model.V1HistoricCandle;

import java.util.List;

import static ru.ilysenko.tinka.helper.CalculationHelper.toDouble;

/**
 * Implementation of the DPO (Detrended price oscillator) indicator
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "create", buildMethodName = "init")
public class DpoIndicator extends AbstractIndicator {
    private int periodsCount = 14;

    private static final double OVERBOUGHT_THRESHOLD = 0.01;
    private static final double OVERSOLD_THRESHOLD = -0.01;

    @Override
    public double calculate(List<V1HistoricCandle> candles) {
        candles = limitCandles(candles, periodsCount + periodsCount / 2 + 1);

        if (candles.size() < periodsCount + periodsCount / 2 + 1) {
            return Double.NaN;
        }
        validate(candles);

        List<V1HistoricCandle> candles4Sma = skipCandles(candles, periodsCount / 2 + 1);
        return toDouble(getLatestCandle(candles).getClose()) - sma(candles4Sma);
    }

    @Override
    public double getOverboughtThreshold() {
        return OVERBOUGHT_THRESHOLD;
    }

    @Override
    public double getOversoldThreshold() {
        return OVERSOLD_THRESHOLD;
    }

    private double sma(List<V1HistoricCandle> candles) {
        double sma = 0;
        for (int i = 0; i < periodsCount; i++) {
            sma += toDouble(candles.get(i).getClose());
        }
        sma /= periodsCount;
        return sma;
    }

    private V1HistoricCandle getLatestCandle(List<V1HistoricCandle> candles) {
        return candles.get(0);
    }
}

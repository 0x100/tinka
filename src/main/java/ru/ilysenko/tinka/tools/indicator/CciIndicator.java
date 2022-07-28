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

import static java.lang.Math.abs;
import static ru.ilysenko.tinka.helper.CalculationHelper.toDouble;

/**
 * Implementation of the CCI (Commodity Channel Index) indicator
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "create", buildMethodName = "init")
public class CciIndicator extends AbstractIndicator {
    private int periodsCount = 14;

    private static final int OVERBOUGHT_THRESHOLD = 100;
    private static final int OVERSOLD_THRESHOLD = -100;

    @Override
    public double calculate(List<V1HistoricCandle> candles) {
        candles = limitCandles(candles, periodsCount);

        if (candles.size() < periodsCount) {
            return Double.NaN;
        }
        validate(candles);

        V1HistoricCandle latestCandle = getLatestCandle(candles);
        double typicalPrice = calcTypicalPrice(latestCandle);
        double sma = sma(candles);
        double mean = mean(candles, sma);

        return (typicalPrice - sma) / (.015 * mean);
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
            sma += calcTypicalPrice(candles.get(i));
        }
        sma /= periodsCount;
        return sma;
    }

    private double mean(List<V1HistoricCandle> candles, double sma) {
        double mean = 0;
        for (int i = 0; i < periodsCount; i++) {
            double typicalPrice = calcTypicalPrice(candles.get(i));
            mean += abs(sma - typicalPrice);
        }
        mean /= periodsCount;
        return mean;
    }

    private double calcTypicalPrice(V1HistoricCandle candle) {
        double high = toDouble(candle.getHigh());
        double low = toDouble(candle.getLow());
        double close = toDouble(candle.getClose());
        return (high + low + close) / 3d;
    }

    private V1HistoricCandle getLatestCandle(List<V1HistoricCandle> candles) {
        return candles.get(0);
    }
}

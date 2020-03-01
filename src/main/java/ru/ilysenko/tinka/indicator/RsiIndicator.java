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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the RSI (Relative Strength Index) indicator
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "create", buildMethodName = "init")
public class RsiIndicator implements Indicator {
    private int periodsCount = 14;

    @Override
    public double calculate(List<Candle> candles) {
        Collections.reverse(candles);
        if (candles.size() < periodsCount + 1) {
            return Double.NaN;
        }
        List<Double> gains = new ArrayList<>();
        List<Double> losses = new ArrayList<>();

        for (int i = candles.size() - 1; i > 0; i--) {
            double todayPrice = candles.get(i).getC();
            double yesterdayPrice = candles.get(i - 1).getC();

            if (todayPrice - yesterdayPrice > 0) {
                gains.add(todayPrice - yesterdayPrice);
                losses.add(0d);
            } else if (todayPrice - yesterdayPrice < 0) {
                losses.add(yesterdayPrice - todayPrice);
                gains.add(0d);
            } else {
                gains.add(0d);
                losses.add(0d);
            }
        }
        double uInitialSma = gains.stream().limit(periodsCount).reduce(0d, Double::sum) / periodsCount;
        double dInitialSma = losses.stream().limit(periodsCount).reduce(0d, Double::sum) / periodsCount;

        if (dInitialSma == 0) {
            return 100d;
        }
        double avgU = uInitialSma;
        double avgD = dInitialSma;

        for (int i = periodsCount; i < candles.size() - 1; i++) {
            avgU = (avgU * (periodsCount - 1) + gains.get(i)) / periodsCount;
            avgD = (avgD * (periodsCount - 1) + losses.get(i)) / periodsCount;
        }
        double rs = avgU / avgD;
        return 100 - 100 / (1 + rs);
    }
}

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
import ru.tinkoff.invest.model.V1HistoricCandle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.Validate.isTrue;
import static ru.ilysenko.tinka.helper.CalculationHelper.smma;
import static ru.ilysenko.tinka.helper.CalculationHelper.toDouble;

/**
 * Implementation of the RSI (Relative Strength Index) indicator
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "create", buildMethodName = "init")
public class RsiIndicator extends AbstractIndicator {
    private int periodsCount = 14;

    private static final int OVERBOUGHT_THRESHOLD = 70;
    private static final int OVERSOLD_THRESHOLD = 30;

    @Override
    public double calculate(List<V1HistoricCandle> candles) {
        candles = reverse(candles);

        if (candles.size() < periodsCount + 1) {
            return Double.NaN;
        }
        validate(candles);

        List<Double> gains = new ArrayList<>();
        List<Double> losses = new ArrayList<>();

        for (int i = 0; i < candles.size() - 1; i++) {
            double todayPrice = toDouble(candles.get(i + 1).getClose());
            double yesterdayPrice = toDouble(candles.get(i).getClose());

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
        double avgU = smma(gains, periodsCount);
        double avgD = smma(losses, periodsCount);

        if (avgD == 0) {
            return 100;
        }
        double rs = avgU / avgD;
        return 100 - 100 / (1 + rs);
    }

    @Override
    protected void validate(List<V1HistoricCandle> candles) {
        isTrue(candles.get(1).getTime().isAfter(candles.get(0).getTime()), "Wrong dates order");
    }

    @Override
    public double getOverboughtThreshold() {
        return OVERBOUGHT_THRESHOLD;
    }

    @Override
    public double getOversoldThreshold() {
        return OVERSOLD_THRESHOLD;
    }

    private List<V1HistoricCandle> reverse(List<V1HistoricCandle> candles) {
        candles = new ArrayList<>(candles);
        Collections.reverse(candles);
        return candles;
    }
}

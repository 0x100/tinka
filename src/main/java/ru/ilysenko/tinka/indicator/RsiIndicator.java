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
 * Implementation of the RSI (Relative Strength Index) indicator
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsiIndicator implements Indicator {
    private int periodsCount = 14;

    @Override
    public double calculate(List<Candle> candles) {
        if (candles.size() < periodsCount) {
            return Double.NaN;
        }
        double uEmaN = 0;
        double dEmaN = 0;

        for (int i = 1; i <= periodsCount; i++) {
            double todayPrice = candles.get(i).getC();
            double yesterdayPrice = candles.get(i - 1).getC();

            if (todayPrice - yesterdayPrice > 0) {
                uEmaN += todayPrice - yesterdayPrice;
            } else if (todayPrice - yesterdayPrice < 0) {
                dEmaN += yesterdayPrice - todayPrice;
            }
        }
        uEmaN /= periodsCount;
        dEmaN /= periodsCount;

        double rs = uEmaN / dEmaN;
        return 100 - (100.0 / (1 + rs));
    }
}
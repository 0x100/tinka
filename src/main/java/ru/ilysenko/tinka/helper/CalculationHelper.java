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
package ru.ilysenko.tinka.helper;

import lombok.experimental.UtilityClass;
import ru.tinkoff.invest.model.V1Quotation;

import java.math.BigDecimal;
import java.util.List;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@UtilityClass
public class CalculationHelper {

    private static final int QUOTATION_SCALE = 9;
    private static final int QUOTATION_MULTIPLIER = 1_000_000_000;


    /**
     * Calculate rate of difference between two numbers
     *
     * @param a number 1
     * @param b number 2
     * @return rate of difference
     */
    public static double calcDifferenceRate(double a, double b) {
        return (b >= a ? (b - a) / a : (a - b) / a) * 100.0;
    }

    /**
     * Calculate rate of difference between two numbers with formatted result
     *
     * @param a number 1
     * @param b number 2
     * @return formatted rate of difference
     */
    public static String differenceRate2String(double a, double b) {
        return (a < b ? "+" : (a == b ? "" : "-")) + format("%.02f", calcDifferenceRate(a, b)) + "%";
    }

    /**
     * Calculating of Smoothed Moving Average (SMMA)
     *
     * @param values values to average
     * @param n      window length (period)
     * @return SMMA
     */
    public static double smma(List<Double> values, int n) {
        double smma = values.stream().limit(n).reduce(0d, Double::sum) / n;
        for (int i = n; i < values.size(); i++) {
            smma = (smma * (n - 1) + values.get(i)) / n;
        }
        return smma;
    }

    public static V1Quotation toQuotation(double value) {
        BigDecimal decimal = BigDecimal.valueOf(value);
        return new V1Quotation()
                .units(String.valueOf(decimal.longValue()))
                .nano(decimal.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(QUOTATION_MULTIPLIER)).intValue());
    }

    public static double toDouble(V1Quotation value) {
        String units = ofNullable(value.getUnits()).orElse("0");
        int nano = ofNullable(value.getNano()).orElse(0);

        return ("0".equals(units) && nano == 0 ? BigDecimal.ZERO : getBigDecimal(units, nano)).doubleValue();
    }

    private static BigDecimal getBigDecimal(String units, int nano) {
        return new BigDecimal(units).add(BigDecimal.valueOf(nano, QUOTATION_SCALE));
    }
}

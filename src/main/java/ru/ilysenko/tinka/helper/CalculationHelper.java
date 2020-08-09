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

import java.util.List;

import static java.lang.String.format;

@UtilityClass
public class CalculationHelper {

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
}

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

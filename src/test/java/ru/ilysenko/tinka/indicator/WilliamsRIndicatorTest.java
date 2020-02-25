package ru.ilysenko.tinka.indicator;

import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WilliamsRIndicatorTest extends IndicatorTest {

    @Test
    void period1() {
        WilliamsRIndicator indicator = WilliamsRIndicator.create().periodsCount(1).init();
        double result = indicator.calculate(getCandles());
        assertEquals("-59,2000", format("%.4f", result));
    }

    @Test
    void period3() {
        WilliamsRIndicator indicator = WilliamsRIndicator.create().periodsCount(3).init();
        double result = indicator.calculate(getCandles());
        assertEquals("-46,5882", format("%.4f", result));
    }

    @Test
    void period5() {
        WilliamsRIndicator indicator = WilliamsRIndicator.create().periodsCount(5).init();
        double result = indicator.calculate(getCandles());
        assertEquals("-56,1427", format("%.4f", result));
    }
}
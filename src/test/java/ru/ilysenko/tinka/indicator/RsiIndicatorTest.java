package ru.ilysenko.tinka.indicator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RsiIndicatorTest extends IndicatorTest {

    @Test
    void period1() {
        Indicator indicator = RsiIndicator.create().periodsCount(1).init();
        double result = indicator.calculate(getCandles());
        assertEquals("100,0", format("%.1f", result));
    }

    @Test
    @Disabled
    void period2() {
        Indicator indicator = RsiIndicator.create().periodsCount(2).init();
        double result = indicator.calculate(getCandles());
        assertEquals("46,5643", format("%.4f", result));
    }

    @Test
    void period4() {
        Indicator indicator = RsiIndicator.create().periodsCount(4).init();
        double result = indicator.calculate(getCandles());
        assertEquals("45,0", format("%.1f", result));
    }
}
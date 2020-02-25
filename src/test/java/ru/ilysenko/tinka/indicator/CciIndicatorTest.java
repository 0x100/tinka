package ru.ilysenko.tinka.indicator;

import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CciIndicatorTest extends IndicatorTest {

    @Test
    void period2() {
        Indicator indicator = CciIndicator.create().periodsCount(2).init();
        double result = indicator.calculate(getCandles());
        assertEquals("66,6667", format("%.4f", result));
    }

    @Test
    void period3() {
        Indicator indicator = CciIndicator.create().periodsCount(3).init();
        double result = indicator.calculate(getCandles());
        assertEquals("10,0592", format("%.4f", result));
    }

    @Test
    void period4() {
        Indicator indicator = CciIndicator.create().periodsCount(4).init();
        double result = indicator.calculate(getCandles());
        assertEquals("46,8973", format("%.4f", result));
    }
}
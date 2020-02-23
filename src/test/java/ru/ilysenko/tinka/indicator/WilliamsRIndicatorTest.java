package ru.ilysenko.tinka.indicator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WilliamsRIndicatorTest extends IndicatorTest {

    @Test
    void calculate() {
        WilliamsRIndicator indicator = WilliamsRIndicator.create().periodsCount(4).init();
        double result = indicator.calculate(getCandles());
        assertTrue(result < 0);
    }
}
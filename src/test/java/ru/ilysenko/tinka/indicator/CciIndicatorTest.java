package ru.ilysenko.tinka.indicator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CciIndicatorTest extends IndicatorTest {

    @Test
    void calculate() {
        CciIndicator indicator = CciIndicator.create().periodsCount(3).init();
        double result = indicator.calculate(getCandles());
        assertTrue(result > 0);
    }
}
package ru.ilysenko.tinka.indicator;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.tinkoff.invest.model.Candle;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RsiIndicatorTest extends IndicatorTest {

    @Test
    void calculate() {
        RsiIndicator indicator = RsiIndicator.create().periodsCount(3).init();
        double result = indicator.calculate(getCandles());
        assertTrue(result > 0);
    }
}
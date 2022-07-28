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

import org.junit.jupiter.api.Test;
import ru.ilysenko.tinka.tools.indicator.Indicator;
import ru.ilysenko.tinka.tools.indicator.WilliamsRIndicator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

class WilliamsRIndicatorTest extends IndicatorTest {

    @Test
    void period1() {
        Indicator indicator = WilliamsRIndicator.create().periodsCount(1).init();
        double result = indicator.calculate(getCandles());
        assertEquals("-59.2000", format(result));
    }

    @Test
    void period3() {
        Indicator indicator = WilliamsRIndicator.create().periodsCount(3).init();
        double result = indicator.calculate(getCandles());
        assertEquals("-46.5882", format(result));
    }

    @Test
    void period5() {
        Indicator indicator = WilliamsRIndicator.create().periodsCount(5).init();
        double result = indicator.calculate(getCandles());
        assertEquals("-56.1427", format(result));
    }

    @Test
    void testOverbought() {
        testOverboughtState(-15);
    }

    @Test
    void testOversold() {
        testOversoldState(-95);
    }

    protected Indicator makeIndicator() {
        return spy(WilliamsRIndicator.class);
    }
}
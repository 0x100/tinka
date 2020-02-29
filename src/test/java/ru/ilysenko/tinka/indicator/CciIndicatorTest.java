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

import java.util.Locale;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CciIndicatorTest extends IndicatorTest {

    @Test
    void period2() {
        Indicator indicator = CciIndicator.create().periodsCount(2).init();
        double result = indicator.calculate(getCandles());
        assertEquals("66.6667", format(Locale.ROOT, "%.4f", result));
    }

    @Test
    void period3() {
        Indicator indicator = CciIndicator.create().periodsCount(3).init();
        double result = indicator.calculate(getCandles());
        assertEquals("10.0592", format(Locale.ROOT, "%.4f", result));
    }

    @Test
    void period4() {
        Indicator indicator = CciIndicator.create().periodsCount(4).init();
        double result = indicator.calculate(getCandles());
        assertEquals("46.8973", format(Locale.ROOT, "%.4f", result));
    }
}
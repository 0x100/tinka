package ru.ilysenko.tinka.helper;

import org.junit.jupiter.api.Test;
import ru.tinkoff.invest.model.V1Quotation;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculationHelperTest {

    @Test
    void toQuotation() {
        V1Quotation value = CalculationHelper.toQuotation(123.45);
        assertEquals("123", value.getUnits());
        assertEquals(450000000, value.getNano());

        value = CalculationHelper.toQuotation(123);
        assertEquals("123", value.getUnits());
        assertEquals(0, value.getNano());

        value = CalculationHelper.toQuotation(.456);
        assertEquals("0", value.getUnits());
        assertEquals(456000000, value.getNano());

        value = CalculationHelper.toQuotation(0);
        assertEquals("0", value.getUnits());
        assertEquals(0, value.getNano());
    }

    @Test
    void toDouble() {
        V1Quotation value = new V1Quotation().units("123").nano(45000);
        assertEquals(123.000045, CalculationHelper.toDouble(value));

        value = new V1Quotation().units("123");
        assertEquals(123, CalculationHelper.toDouble(value));

        value = new V1Quotation().nano(45);
        assertEquals(0.000000045, CalculationHelper.toDouble(value));

        value = new V1Quotation();
        assertEquals(0, CalculationHelper.toDouble(value));
    }
}
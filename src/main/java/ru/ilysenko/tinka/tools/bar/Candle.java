package ru.ilysenko.tinka.tools.bar;

import lombok.Data;
import org.threeten.bp.OffsetDateTime;

@Data
public class Candle {
    private double open;
    private double close;
    private double high;
    private double low;
    private double volume;
    private boolean completed;
    private OffsetDateTime time;
}

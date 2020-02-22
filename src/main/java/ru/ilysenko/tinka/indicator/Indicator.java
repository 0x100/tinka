package ru.ilysenko.tinka.indicator;

import ru.tinkoff.invest.model.Candle;

import java.util.List;

public interface Indicator {

    double calculate(List<Candle> candles);
}

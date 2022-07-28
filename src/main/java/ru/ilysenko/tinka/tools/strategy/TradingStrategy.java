package ru.ilysenko.tinka.tools.strategy;

import ru.ilysenko.tinka.tools.strategy.enums.TradingAdvice;
import ru.tinkoff.invest.model.V1HistoricCandle;

import java.util.List;

public interface TradingStrategy {

    TradingAdvice solve(List<V1HistoricCandle> candles);
}

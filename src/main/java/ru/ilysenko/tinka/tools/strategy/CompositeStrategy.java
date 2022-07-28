package ru.ilysenko.tinka.tools.strategy;

import ru.ilysenko.tinka.tools.strategy.enums.TradingAdvice;
import ru.tinkoff.invest.model.V1HistoricCandle;

import java.util.Arrays;
import java.util.List;

public class CompositeStrategy implements TradingStrategy {

    private final List<TradingStrategy> strategies;

    private double threshold = 1;


    public CompositeStrategy(TradingStrategy... strategies) {
        this.strategies = Arrays.asList(strategies);
    }

    public CompositeStrategy(double threshold, TradingStrategy... strategies) {
        this(strategies);
        this.threshold = threshold;
    }

    @Override
    public TradingAdvice solve(List<V1HistoricCandle> candles) {
        int buyCount = 0;
        int sellCount = 0;

        for (TradingStrategy strategy : strategies) {
            TradingAdvice decision = strategy.solve(candles);
            switch (decision) {
                case BUY -> buyCount++;
                case SELL -> sellCount++;
            }
        }
        double strategiesCount = strategies.size();
        if (buyCount / strategiesCount >= threshold) {
            return TradingAdvice.BUY;
        } else if (sellCount / strategiesCount >= threshold) {
            return TradingAdvice.SELL;
        }
        return TradingAdvice.NONE;
    }
}

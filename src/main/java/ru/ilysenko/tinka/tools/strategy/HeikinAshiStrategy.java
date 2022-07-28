package ru.ilysenko.tinka.tools.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.ilysenko.tinka.helper.CalculationHelper;
import ru.ilysenko.tinka.mapper.CandleMapper;
import ru.ilysenko.tinka.tools.bar.Candle;
import ru.ilysenko.tinka.tools.bar.HeikinAshi;
import ru.ilysenko.tinka.tools.strategy.enums.TradingAdvice;
import ru.ilysenko.tinka.tools.strategy.enums.Trend;
import ru.tinkoff.invest.model.V1HistoricCandle;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HeikinAshiStrategy implements TradingStrategy {

    private static final int HEIKIN_ASHI_CANDLES_COUNT = 3;
    public static final int MIN_CANDLES_SIZE = HEIKIN_ASHI_CANDLES_COUNT + 1;

    private final CandleMapper candleMapper;


    @Override
    public TradingAdvice solve(List<V1HistoricCandle> candles) {
        if (candles.size() < MIN_CANDLES_SIZE) {
            return TradingAdvice.NONE;
        }
        List<Candle> heikinAshis = getHeikinAshis(candles);
        Candle candle1 = heikinAshis.get(0);
        Candle candle2 = heikinAshis.get(1);
        Candle currentCandle = heikinAshis.get(heikinAshis.size() - 1);

        log.debug("---------------------------------------");

        double v1;
        double v2;

        if (candle2.getHigh() < candle1.getHigh()) {
            v1 = candle1.getHigh();
            v2 = candle2.getHigh();
        } else if (candle2.getLow() > candle1.getLow()) {
            v1 = candle1.getLow();
            v2 = candle2.getLow();
        } else {
            v1 = candle1.getClose();
            v2 = candle2.getClose();
        }
        double angle = CalculationHelper.calcAngleBetweenCandles(candle1, candle2);
        log.debug("Angle between {}, {}: {}", v1, v2, angle);

        Trend trend = CalculationHelper.getTrend(angle);
        log.debug("Trend: {} {}", trend, currentCandle.getTime());

        if (trend == Trend.FLAT) {
            return TradingAdvice.NONE;
        }
        boolean isTrendUp = trend == Trend.UP;

        double line1Y = getExtremeValue(candle1, isTrendUp);
        double line2Y = getExtremeValue(currentCandle, isTrendUp);
        double lineDeviation = line2Y - line1Y;

        return getTradingAdvice(trend, lineDeviation);
    }

    private TradingAdvice getTradingAdvice(Trend trend, double lineDeviation) {
        boolean isTrendUp = trend == Trend.UP;
        boolean isTrendDown = trend == Trend.DOWN;

        if (isTrendUp && lineDeviation < 0) {
            return TradingAdvice.SELL;
        }
        if (isTrendDown && lineDeviation > 0) {
            return TradingAdvice.BUY;
        }
        return TradingAdvice.NONE;
    }

    private double getExtremeValue(Candle candle, boolean isTrendUp) {
        return isTrendUp ? candle.getLow() : candle.getHigh();
    }

    public List<Candle> getHeikinAshis(List<V1HistoricCandle> candles) {
        List<Candle> list = new ArrayList<>(HEIKIN_ASHI_CANDLES_COUNT);
        int candlesCount = candles.size();
        Candle prevCandle = candleMapper.toCandle(candles.get(0));

        for (int i = candlesCount - HEIKIN_ASHI_CANDLES_COUNT; i < candlesCount; i++) {
            Candle candle = candleMapper.toCandle(candles.get(i));
            Candle heikinAshi = new HeikinAshi(prevCandle, candle);

            list.add(heikinAshi);
            prevCandle = heikinAshi;
        }
        return list;
    }
}

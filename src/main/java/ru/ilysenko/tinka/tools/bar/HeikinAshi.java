package ru.ilysenko.tinka.tools.bar;

import lombok.RequiredArgsConstructor;
import org.threeten.bp.OffsetDateTime;

import static java.lang.Double.max;
import static java.lang.Double.min;

@RequiredArgsConstructor
public class HeikinAshi extends Candle {

    private static final String ZONE_OFFSET = "+03:00";
    private final Candle prevCandle;
    private final Candle candle;


    @Override
    public double getOpen() {
        return (prevCandle.getOpen() + candle.getOpen()) / 2d;
    }

    @Override
    public double getClose() {
        return (candle.getOpen() + candle.getClose() + candle.getHigh() + candle.getLow()) / 4d;
    }

    @Override
    public double getHigh() {
        return max(candle.getHigh(), max(candle.getOpen(), candle.getClose()));
    }

    @Override
    public double getLow() {
        return min(candle.getLow(), min(candle.getOpen(), candle.getClose()));
    }

    @Override
    public double getVolume() {
        return (prevCandle.getVolume() + candle.getVolume()) / 2d;
    }

    @Override
    public boolean isCompleted() {
        return candle.isCompleted();
    }

    @Override
    public OffsetDateTime getTime() {
//        return candle.getTime().withOffsetSameLocal(ZoneOffset.of(ZONE_OFFSET));
        return candle.getTime().plusHours(3);
    }
}

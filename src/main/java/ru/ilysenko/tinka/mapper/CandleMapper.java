package ru.ilysenko.tinka.mapper;

import org.jfree.data.xy.OHLCDataItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ilysenko.tinka.helper.CalculationHelper;
import ru.ilysenko.tinka.tools.bar.Candle;
import ru.ilysenko.tinka.tools.bar.HeikinAshi;
import ru.tinkoff.invest.model.V1HistoricCandle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring", imports = {CalculationHelper.class})
public interface CandleMapper {

    @Mapping(target = "open", expression = "java(CalculationHelper.toDouble(source.getOpen()))")
    @Mapping(target = "close", expression = "java(CalculationHelper.toDouble(source.getClose()))")
    @Mapping(target = "high", expression = "java(CalculationHelper.toDouble(source.getHigh()))")
    @Mapping(target = "low", expression = "java(CalculationHelper.toDouble(source.getLow()))")
    @Mapping(target = "volume", source = "volume")
    @Mapping(target = "time", source = "time")
    @Mapping(target = "completed", source = "isComplete")
    Candle toCandle(V1HistoricCandle source);

    List<Candle> toCandles(List<V1HistoricCandle> source);

    default List<Candle> toHeikinAshis(List<Candle> candles) {
        List<Candle> result = new ArrayList<>(candles.size() - 1);
        Candle prevCandle = candles.get(0);
        for (int i = 1; i < candles.size(); i++) {
            Candle candle = candles.get(i);
            HeikinAshi heikinAshi = new HeikinAshi(prevCandle, candle);
            result.add(heikinAshi);

            prevCandle = heikinAshi;
        }
        return result;
    }

    default OHLCDataItem toOHLCDataItem(Candle candle) {
        long time = candle.getTime().toInstant().toEpochMilli();
        return new OHLCDataItem(new Date(time), candle.getOpen(), candle.getHigh(), candle.getLow(), candle.getClose(), candle.getVolume());
    }

    OHLCDataItem[] toOHLCDataItems(List<Candle> candles);
}

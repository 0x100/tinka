package ru.ilysenko.tinka.chart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ilysenko.tinka.mapper.CandleMapper;

@Component
@RequiredArgsConstructor
public class ChartManager {

    private final CandleMapper candleMapper;



}

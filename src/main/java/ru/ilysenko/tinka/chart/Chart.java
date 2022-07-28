package ru.ilysenko.tinka.chart;

import lombok.SneakyThrows;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataset;
import ru.ilysenko.tinka.mapper.CandleMapper;
import ru.ilysenko.tinka.mapper.CandleMapperImpl;
import ru.ilysenko.tinka.tools.bar.Candle;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class Chart {

    private static final CandleMapper CANDLE_MAPPER = new CandleMapperImpl();

    private JFreeChart innerChart;


    private Chart() {
    }

    public static Chart newInstance(List<Candle> candles) {
        Chart chart = new Chart();
        chart.innerChart = createChart(candles);
        return chart;
    }

    private static JFreeChart createChart(List<Candle> candles) {
//        OHLCDataset dataset = prepareDataset(candles);
//        JFreeChart chart = ChartFactory.createCandlestickChart(null, null, null, dataset, false);
//        XYPlot plot = chart.getXYPlot();
//        plot.setOrientation(PlotOrientation.VERTICAL);
////        plot.setRenderer(new CandlestickRenderer());
//
//        XYDataset dataset = null;// your line dataset
//        int datasetCount = plot.getDatasetCount();
//        plot.setDataset(datasetCount, dataset);
//
//        XYLineAndShapeRenderer lineRenderer = new XYLineAndShapeRenderer(true, false);
//        plot.setRenderer(datasetCount, lineRenderer);
//
//        NumberAxis range = (NumberAxis) plot.getRangeAxis();
//        range.setUpperMargin(.1);
//        range.setLowerMargin(.1);
//        range.setAutoRange(true);
//        range.setAutoRangeIncludesZero(false);
//
//        return chart;

        return null;
    }

    @SneakyThrows
    public void saveToFile(String fileName) {
        assert innerChart != null;

        try (OutputStream out = new FileOutputStream(fileName)) {
            ChartUtilities.writeChartAsPNG(out,
                    innerChart,
                    1000,
                    600);
        }

    }

    private static OHLCDataset prepareDataset(List<Candle> candles) {
        return new DefaultOHLCDataset("Series", CANDLE_MAPPER.toOHLCDataItems(candles));
    }
}

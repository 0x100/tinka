package ru.ilysenko.tinka.chart;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class CandlestickRenderer extends org.jfree.chart.renderer.xy.CandlestickRenderer {

    private final Paint colorRaising = Color.GREEN;
    private final Paint colorFalling = Color.RED;
    private final Paint colorUnknown = Color.GRAY;
    private final Paint colorTransparent = Color.BLACK;

    public CandlestickRenderer() {
        setDrawVolume(false);
        setUpPaint(colorUnknown); // use unknown color if error
        setDownPaint(colorUnknown); // use unknown color if error
    }


    @Override
    public Paint getItemPaint(int series, int item) {
        OHLCDataset highLowData = (OHLCDataset) getPlot().getDataset(series);
        Number curClose = highLowData.getClose(series, item);
        Number prevClose = highLowData.getClose(series, item > 0 ? item - 1 : 0);

        if (prevClose.doubleValue() <= curClose.doubleValue()) {
            return Color.GREEN;
        } else {
            return Color.RED;
        }
    }

    @Override
    public void drawItem(Graphics2D g2, XYItemRendererState state,
                         Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot,
                         ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset,
                         int series, int item, CrosshairState crosshairState, int pass) {

        OHLCDataset highLowData = (OHLCDataset) dataset;
        double yOpen = highLowData.getOpenValue(series, item);
        double yClose = highLowData.getCloseValue(series, item);

        // set color for filled candle
        if (yClose >= yOpen) {
            setUpPaint(colorRaising);
            setDownPaint(colorFalling);
        }

        // set color for hollow (not filled) candle
        else {
            setUpPaint(colorTransparent);
            setDownPaint(colorTransparent);
        }

        // call parent method
        super.drawItem(g2, state, dataArea, info, plot, domainAxis, rangeAxis, dataset, series, item, crosshairState, pass);
    }

}
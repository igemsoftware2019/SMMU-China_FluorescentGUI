package MainGUI;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.math.BigDecimal;

public class Chart {

    private static XYDataset dataset(double vx) {
        double vy = vx * 13.01 + 63.46;
        XYSeries predict_xy = new XYSeries("Predict Protein Concentration");
        predict_xy.add(0.0, 0.0);
        predict_xy.add(8.463, 200.0);
        predict_xy.add(27.975, 400.0);
        predict_xy.add(36.814, 600.0);
        predict_xy.add(50.097, 800.0);
        predict_xy.add(77.751, 1000.0);
        predict_xy.add(90.0, 1234.36); //predicted
        XYSeries recognized_xy = new XYSeries("Recognized Value");
        recognized_xy.add(0.0, vy);
        recognized_xy.add(vx, vy);
        recognized_xy.add(vx, 0.0);
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        xySeriesCollection.addSeries(predict_xy);
        xySeriesCollection.addSeries(recognized_xy);
        return xySeriesCollection;
    }

    public void dochart(double recog_x, double zero) {
        Controller.controller(80, "Making Chart");
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Standard Curve",
                "[Value X] Relevant Light Intensity",
                "[Value Y] Relevent Fluorescent Protein",
                dataset(recog_x - zero),
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        chart.setBorderPaint(Color.DARK_GRAY);
        Controller.controller(95, "Almost Ready!");
        double recog_y = (recog_x - zero) * 13.01 + 63.46;
        Double r_x = new BigDecimal(recog_x - zero).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        Double r_y = new BigDecimal(recog_y).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        chart.addSubtitle(new TextTitle("Recognized X: " + r_x + "    " + "Recognized Y: " + r_y));
        ChartFrame frame = new ChartFrame("Criteria Figure", chart, true);
        frame.pack();
        frame.setVisible(true);
        Controller.controller(100, "Process Accomplished!");
    }
}

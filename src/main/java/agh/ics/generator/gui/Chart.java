package agh.ics.generator.gui;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

public class Chart {
    private final NumberAxis xAxis;
    private final NumberAxis yAxis;
    private final LineChart<Number,Number> lineChart;
    private final String title;
    XYChart.Series series;
    int cnt = 0;

    public Chart(String title){
        this.xAxis = new NumberAxis();
        this.yAxis = new NumberAxis();
        this.lineChart = new LineChart<>(this.xAxis,this.yAxis);
        this.title = title;
        this.series = new XYChart.Series();
        lineChart.setTitle(title);
        lineChart.getData().add(series);
    }

    public VBox updateChart(int x, int y){
        this.series.getData().add(new XYChart.Data(cnt, cnt));
        this.lineChart.setPrefSize(150,200);
        cnt += 1;


        return new VBox(lineChart);
    }
}

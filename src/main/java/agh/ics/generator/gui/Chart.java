package agh.ics.generator.gui;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

public class Chart {
    private final NumberAxis xAxis = new NumberAxis();
    private final NumberAxis yAxis = new NumberAxis();
    private final LineChart<Number,Number> lineChart = new LineChart(this.xAxis,this.yAxis);
    private final String title;
    XYChart.Series series = new XYChart.Series();

    public Chart(String title){
        this.title = title;
        lineChart.setTitle(title);
        lineChart.getData().add(series);
        this.lineChart.setPrefSize(150,200);
        this.series.getData().add(new XYChart.Data<>(0, 0));
    }

    public void updateChart(Number x, Number y){
        this.series.getData().add(new XYChart.Data<>(x, y));
        this.lineChart.setPrefSize(150,200);

    }

    public VBox getChart(){
        return new VBox(this.lineChart);
    }
}

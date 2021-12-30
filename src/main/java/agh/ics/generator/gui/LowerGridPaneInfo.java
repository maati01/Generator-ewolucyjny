package agh.ics.generator.gui;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class LowerGridPaneInfo extends GridPane {
//    Genotype genotype;
    List<Chart> charts;

    public LowerGridPaneInfo(List<Chart> charts){
        this.charts = charts;
//        this.genotype = genotype;
        createGrid();
    }

    public void createGrid(){
        HBox box1 = new HBox(this.charts.get(0).getChart(),this.charts.get(1).getChart());
        HBox box2 = new HBox(this.charts.get(2).getChart(),this.charts.get(3).getChart());
        this.add(new VBox(box1,box2),0,0);
    }
}

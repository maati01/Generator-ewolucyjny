package agh.ics.generator.gui;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

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
        HBox box = new HBox(this.charts.get(2).updateChart(0,0),this.charts.get(3).updateChart(0,0));
        this.add(box,0,0);
    }
}

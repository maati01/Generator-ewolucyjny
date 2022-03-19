package agh.ics.generator.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class MiddleGridPaneInfo extends GridPane {
    Label mapTitle;
    public Button save;
    public Button pause;
    CheckBox normalEvolution;
    CheckBox magicEvolution;

    public Button getSave(){
        return this.save;
    }

    public Button getPause(){
        return this.pause;
    }

    public MiddleGridPaneInfo(String mapTitle){
        this.mapTitle = new Label(mapTitle);
        this.save = new Button("Save");
        this.pause = new Button("Pause");
        this.normalEvolution = new CheckBox("Normal evolution");
        this.magicEvolution = new CheckBox("Magic evolution");
        setLabelParameters();
        createGrid();
    }

    public void setLabelParameters(){
        this.mapTitle.setAlignment(Pos.CENTER);
        this.mapTitle.setMaxSize(200,100);
        this.mapTitle.setFont(new Font(25));
    }

    public void createGrid(){
        HBox box = new HBox(pause,save);
        box.setPadding(new Insets(5,0,5,0));
        box.setSpacing(10);

        normalEvolution.setPadding(new Insets(5,0,5,0));
        magicEvolution.setPadding(new Insets(5,0,5,0));

        this.add(mapTitle,0,0,5,1);
        this.add(box,0,1);
        this.add(normalEvolution,0,2);
        this.add(magicEvolution,0,3);

        this.setPadding(new Insets(10,0,10,0));
    }
}

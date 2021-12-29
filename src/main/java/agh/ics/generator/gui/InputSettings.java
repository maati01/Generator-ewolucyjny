package agh.ics.generator.gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;

public class InputSettings extends Parent {
    private final TilePane tilePane;
    private final TextField width;
    private final TextField height;
    private final TextField startEnergy;
    private final TextField moveEnergy;
    private final TextField plantEnergy;
    private final TextField jungleRatio;
    private final HBox widthBox;
    private final HBox heightBox;
    private final HBox startEnergyBox;
    private final HBox moveEnergyBox;
    private final HBox plantEnergyBox;
    private final HBox jungleRatioBox;

    public InputSettings(Button button){
        this.width = new TextField();
        this.height = new TextField();
        this.startEnergy = new TextField();
        this.moveEnergy = new TextField();
        this.plantEnergy = new TextField();
        this.jungleRatio = new TextField();

        Label widthLabel = new Label("Width: ");
        Label heightLabel = new Label("Height: ");
        Label startEnergyLabel = new Label("Start energy: ");
        Label moveEnergyLabel = new Label("Move energy: ");
        Label plantEnergyLabel = new Label("Plant energy: ");
        Label jungleRatioLabel = new Label("Jungle ratio: ");

        this.widthBox = new HBox(widthLabel,this.width);
        this.heightBox = new HBox(heightLabel,this.height);
        this.startEnergyBox = new HBox(startEnergyLabel,this.startEnergy);
        this.moveEnergyBox = new HBox(moveEnergyLabel,this.moveEnergy);
        this.plantEnergyBox = new HBox(plantEnergyLabel,this.plantEnergy);
        this.jungleRatioBox = new HBox(jungleRatioLabel,this.jungleRatio);


        this.tilePane = new TilePane(this.widthBox,this.heightBox,this.startEnergyBox,
                this.moveEnergyBox,this.plantEnergyBox,this.jungleRatioBox,button);

        setParameters();
    }

    public TilePane getTilePane(){
        return tilePane;
    }

    public TextField getWidth(){
        return this.width;
    }

    public TextField getHeight(){
        return this.height;
    }

    public TextField getStartEnergy(){
        return this.startEnergy;
    }

    public TextField getMoveEnergy(){
        return this.moveEnergy;
    }

    public TextField getPlantEnergy(){
        return this.plantEnergy;
    }

    public TextField getJungleRatio(){
        return this.jungleRatio;
    }

    public void setParameters(){
        this.widthBox.setSpacing(42);
        this.heightBox.setSpacing(39);
        this.startEnergyBox.setSpacing(10);
        this.moveEnergyBox.setSpacing(5);
        this.plantEnergyBox.setSpacing(8);
        this.jungleRatioBox.setSpacing(13);

        tilePane.setPadding(new Insets(10,10,10,10));
        tilePane.setHgap(10);
        tilePane.setVgap(15);
    }
    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}

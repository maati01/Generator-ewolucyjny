package agh.ics.generator.gui;

import agh.ics.generator.AbstractWorldMapElement;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    AbstractWorldMapElement element;

    public GuiElementBox(AbstractWorldMapElement element){
        this.element = element;
    }

    public VBox getImage() throws FileNotFoundException {
        Image image = new Image(new FileInputStream(this.element.getImagePath()));
        ImageView imageView = new ImageView(image);
        Label label = new Label(this.element.toStringInGui());
        VBox verticalBox = new VBox(imageView,label);

        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        verticalBox.setAlignment(Pos.CENTER);

        return verticalBox;
    }

}

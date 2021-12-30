package agh.ics.generator.gui;

import agh.ics.generator.mapelements.AbstractWorldMapElement;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    AbstractWorldMapElement element;
    int imageWidth;
    int imageHeight;

    public GuiElementBox(AbstractWorldMapElement element,int imageWidth, int imageHeight){
        this.element = element;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public VBox getImage() throws FileNotFoundException {
        Image image = new Image(new FileInputStream(this.element.getImagePath()));
        ImageView imageView = new ImageView(image);
        VBox verticalBox = new VBox(imageView);

        imageView.setFitWidth(this.imageWidth);
        imageView.setFitHeight(this.imageHeight);
        verticalBox.setAlignment(Pos.CENTER);

        return verticalBox;
    }

}

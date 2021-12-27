package agh.ics.generator.gui;


import agh.ics.generator.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App extends Application implements IAnimalMoveObserver {
    private final GridPane gridPane = new GridPane();
    private AbstractWorldMap wrappedMap;
    private AbstractWorldMap boundedMap;
    Thread simulationEngineThread;
    int screenWidth = 2000;
    int screenHeight = 1000;
    int mapWidth = 20;
    int mapHeight = 30;
    int fieldWidth = (int) Math.ceil(screenWidth/(mapWidth*2 + 1));
    int fieldHeight = fieldWidth;


    //TODO
    //przekazywac do silnika dwie mapy
    //problem z rysowaniem obiektów

    @Override
    public void init() throws Exception {
        try {
            List<Vector2d> positions = new ArrayList<>(Arrays.asList(new Vector2d(1, 1), new Vector2d(1, 1)));
            this.wrappedMap = new WrappedGrassField(0.2);
            this.boundedMap = new BoundedGrassField(0.2);
            SimulationEngine engine = new SimulationEngine(this.wrappedMap,this.boundedMap, positions);
            this.simulationEngineThread = new Thread(engine);
            engine.addObserver(this);
            simulationEngineThread.start();

        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void animalMove() {
        Platform.runLater(() -> {
            this.gridPane.getChildren().clear();
            this.gridPane.getRowConstraints().clear();
            this.gridPane.getColumnConstraints().clear();
            showResult();});
    }

    public void drawGrid() {
        this.gridPane.setGridLinesVisible(false);
        this.gridPane.setGridLinesVisible(true);

        Vector2d lowerLeft = this.wrappedMap.getLowerLeft();
        Vector2d upperRight = this.wrappedMap.getUpperRight();

        int i = 0;
        for (int x = lowerLeft.x; x <= upperRight.x; x++) {

            int j = 0;
            for (int y = lowerLeft.y; y <= upperRight.y; y++) {
                drawObjectOnWrappedMap(new Vector2d(x,y),i,j);
                drawObjectOnBoundedMap(new Vector2d(x,y),i,j);
                j++;
            }
            i++;
        }
    }

    public void drawCenterLine() {
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(fieldWidth);
        rectangle.setHeight(screenHeight);
        rectangle.setStroke(Color.TRANSPARENT);
        rectangle.setFill(Color.valueOf("black"));
        this.gridPane.add(rectangle,this.wrappedMap.getUpperRight().x - this.wrappedMap.getLowerLeft().x + 1,0);
    }

    public void drawObjectOnWrappedMap(Vector2d vector,int i,int j){
        AbstractWorldMapElement object = this.wrappedMap.objectAt(vector);
        if(object != null) {
            GuiElementBox guiElement = new GuiElementBox(object);
            try {
                this.gridPane.add(guiElement.getImage(), i, this.wrappedMap.getUpperRight().y - this.wrappedMap.getLowerLeft().y - j);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void drawObjectOnBoundedMap(Vector2d vector,int i,int j){
        AbstractWorldMapElement object = this.boundedMap.objectAt(vector);
        if(object != null) {
            GuiElementBox guiElement = new GuiElementBox(object);
            try {
                this.gridPane.add(guiElement.getImage(), this.wrappedMap.getUpperRight().x - this.wrappedMap.getLowerLeft().x + i + 2,
                        this.wrappedMap.getUpperRight().y - this.wrappedMap.getLowerLeft().y - j);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void showResult() {
        drawGrid();
        setConstraints();
        drawCenterLine();
    }

    public void setConstraints() {
        for(int i = 0; i <= this.wrappedMap.getUpperRight().y - this.wrappedMap.getLowerLeft().y; i++){
            this.gridPane.getRowConstraints().add(new RowConstraints(this.fieldHeight));
        }
        for(int i = 0; i <= (this.wrappedMap.getUpperRight().x - this.wrappedMap.getLowerLeft().x + 1)*2; i++){
            this.gridPane.getColumnConstraints().add(new ColumnConstraints(this.fieldWidth));
        }
    }

}

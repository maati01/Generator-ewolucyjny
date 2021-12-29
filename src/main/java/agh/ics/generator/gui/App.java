package agh.ics.generator.gui;


import agh.ics.generator.*;
import agh.ics.generator.interfaces.IAnimalMoveObserver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    int screenWidth = 1500;
    int screenHeight = 700;
    int mapWidth;
    int mapHeight;
    int fieldWidth;
    int fieldHeight;


    //TODO
    //przekazywac do silnika dwie mapy
    //problem z rysowaniem obiektów

    @Override
    public void init() throws Exception {
        try {

            this.wrappedMap = new WrappedGrassField(0.2);
            this.boundedMap = new BoundedGrassField(0.2);



        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Generator ewolucyjny");
        Button button = new Button("Start");
        InputSettings input = new InputSettings(button);

        button.setOnAction(action -> {
            setInputSettings(input);
            List<Vector2d> positions = new ArrayList<>(Arrays.asList(new Vector2d(1, 1), new Vector2d(1, 1)));
            SimulationEngine engine = new SimulationEngine(this.wrappedMap,this.boundedMap, positions);
            this.simulationEngineThread = new Thread(engine); //TODO to chyba nie powinno tu byc, wynieść to
            engine.addObserver(this);
            Scene scene = new Scene(gridPane,1500,700);
            primaryStage.setScene(scene);
            primaryStage.show();
            simulationEngineThread.start();
        });


        Scene scene = new Scene(input.getTilePane(), 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void setInputSettings(InputSettings input){
        this.mapWidth = Integer.parseInt(input.getWidth().getText());
        this.mapHeight = Integer.parseInt(input.getHeight().getText());
        this.wrappedMap.setHeight(this.mapHeight);
        this.wrappedMap.setWidth(this.mapWidth);
        this.boundedMap.setHeight(this.mapHeight);
        this.boundedMap.setWidth(this.mapWidth);
        this.boundedMap.findJungleCorners(); //TODO to zrobić inaczej
        this.boundedMap.findStepAndJunglePositions();
        this.wrappedMap.findJungleCorners();
        this.wrappedMap.findStepAndJunglePositions();
        this.fieldWidth = (int) Math.ceil(screenWidth/(mapWidth*2 + 1))/2; //TODO sprytniej wyliczac zmienne
        this.fieldHeight = fieldWidth;


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

        int i = 0;
        for (int x = 0; x <= this.mapWidth; x++) {

            int j = 0;
            for (int y = 0; y <= this.mapHeight; y++) {
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
        this.gridPane.add(rectangle,this.mapWidth + 1,0);
    }

    public void drawObjectOnWrappedMap(Vector2d vector,int i,int j){
        AbstractWorldMapElement object;
        if(this.wrappedMap.getAnimalOnMap(vector) != null){
            object = this.wrappedMap.getAnimalOnMap(vector);
        }else if(this.wrappedMap.getGrassOnMap(vector) != null){
            object = this.wrappedMap.getGrassOnMap(vector);
        }else{
            object = null;
        }

        if(object != null) {
            GuiElementBox guiElement = new GuiElementBox(object);
            try {
                this.gridPane.add(guiElement.getImage(), i, this.mapHeight - j);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void drawObjectOnBoundedMap(Vector2d vector,int i,int j){
        AbstractWorldMapElement object;
        if(this.boundedMap.getAnimalOnMap(vector) != null){
            object = this.boundedMap.getAnimalOnMap(vector);
        }else if(this.boundedMap.getGrassOnMap(vector) != null){
            object = this.boundedMap.getGrassOnMap(vector);
        }else{
            object = null;
        }

        if(object != null) {
            GuiElementBox guiElement = new GuiElementBox(object);
            try {
                this.gridPane.add(guiElement.getImage(), this.mapWidth + i + 2,
                        this.mapHeight - j);
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
        for(int i = 0; i <= this.mapHeight; i++){
            this.gridPane.getRowConstraints().add(new RowConstraints(this.fieldHeight));
        }
        for(int i = 0; i <= (mapWidth + 1)*2; i++){
            this.gridPane.getColumnConstraints().add(new ColumnConstraints(this.fieldWidth));
        }
    }

}

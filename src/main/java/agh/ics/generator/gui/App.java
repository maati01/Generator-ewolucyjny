package agh.ics.generator.gui;


import agh.ics.generator.interfaces.IAnimalMoveObserver;
import agh.ics.generator.mapelements.AbstractWorldMapElement;
import agh.ics.generator.mapelements.Vector2d;
import agh.ics.generator.maps.AbstractWorldMap;
import agh.ics.generator.maps.BoundedGrassField;
import agh.ics.generator.maps.WrappedGrassField;
import agh.ics.generator.simulation.EpochStatistic;
import agh.ics.generator.simulation.SimulationEngine;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class App extends Application implements IAnimalMoveObserver {
    private final GridPane gridPaneContainer = new GridPane();
    private final GridPane gridPaneForWrappedMap = new GridPane();
    private final GridPane gridPaneForBoundedMap = new GridPane();

    private AbstractWorldMap wrappedMap;
    private AbstractWorldMap boundedMap;
    Thread simulationEngineThreadWrappedMap;
    Thread simulationEngineThreadBoundedMap;

    int screenWidth = 1500;
    int screenHeight = 800;
    int mapWidth;
    int mapHeight;
    int fieldWidth;
    int fieldHeight;
    int startEnergy;
    int moveEnergy;
    int plantEnergy;
    double jungleRatio;
    int numberOfStartingAnimals;
    int imageWidth;
    int imageHeight;

    ChartHandler wrappedMapChartUpdate;
    ChartHandler boundedMapChartUpdate;

    MiddleGridPaneInfo gridInfoWrappedMap;
    MiddleGridPaneInfo gridInfoBoundedMap;

    SimulationEngine wrappedMapEngine;
    SimulationEngine boundedMapEngine;

    @Override
    public void init() throws Exception {
        try {
            Platform.setImplicitExit(false);

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

        Scene scene = new Scene(input.getTilePane(), 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        button.setOnAction(action -> {
            primaryStage.close();

            setInputSettings(input);

            this.wrappedMap = new WrappedGrassField(this.jungleRatio,this.mapWidth,this.mapHeight,
                    this.numberOfStartingAnimals,this.startEnergy,this.moveEnergy,this.plantEnergy);
            this.boundedMap = new BoundedGrassField(this.jungleRatio,this.mapWidth,this.mapHeight,
                    this.numberOfStartingAnimals,this.startEnergy,this.moveEnergy,this.plantEnergy);

            this.wrappedMapChartUpdate = new ChartHandler(this.wrappedMap);
            this.boundedMapChartUpdate = new ChartHandler(this.wrappedMap);
            prepareLayout();

            this.wrappedMapEngine = new SimulationEngine(this.wrappedMap, this.wrappedMapChartUpdate);
            this.boundedMapEngine = new SimulationEngine(this.boundedMap, this.boundedMapChartUpdate);
            this.simulationEngineThreadWrappedMap = new Thread(wrappedMapEngine);
            this.simulationEngineThreadBoundedMap = new Thread(boundedMapEngine);
            wrappedMapEngine.addObserver(this);
            boundedMapEngine.addObserver(this);

            Scene new_scene = new Scene(this.gridPaneContainer,this.screenWidth,this.screenHeight);
            primaryStage.setScene(new_scene);
            primaryStage.show();
            this.simulationEngineThreadWrappedMap.start();
            this.simulationEngineThreadBoundedMap.start();

            buttonsHandler();

        });


    }

    public void prepareLayout(){
        this.gridInfoWrappedMap = new MiddleGridPaneInfo("Wrapped map");
        this.gridInfoBoundedMap = new MiddleGridPaneInfo("Bounded map");
        LowerGridPaneInfo gridLowerInfoWrappedMap = new LowerGridPaneInfo(this.wrappedMapChartUpdate.getMapCharts());
        LowerGridPaneInfo gridLowerInfoBoundedMap = new LowerGridPaneInfo(this.boundedMapChartUpdate.getMapCharts());

        this.gridPaneContainer.add(gridInfoWrappedMap,1,0);
        this.gridPaneContainer.add(gridInfoBoundedMap,3,0);
        this.gridPaneContainer.add(gridLowerInfoWrappedMap,0,1);
        this.gridPaneContainer.add(gridLowerInfoBoundedMap,2,1);

        this.gridPaneContainer.add(this.gridPaneForWrappedMap,0,0,1,1);
        this.gridPaneContainer.add(this.gridPaneForBoundedMap,2,0,1,1);
    }


    public void setInputSettings(InputSettings input){
        this.mapWidth = Integer.parseInt(input.getWidth().getText());
        this.mapHeight = Integer.parseInt(input.getHeight().getText());
        this.jungleRatio = Double.parseDouble(input.getJungleRatio().getText());
        this.startEnergy = Integer.parseInt(input.getStartEnergy().getText());
        this.moveEnergy = Integer.parseInt(input.getMoveEnergy().getText());
        this.plantEnergy = Integer.parseInt(input.getPlantEnergy().getText());
        this.numberOfStartingAnimals = Integer.parseInt(input.getNumberOfStartingAnimals().getText());

        this.fieldWidth = ((screenWidth-500)/2)/this.mapWidth; //TODO sprytniej wyliczac zmienne
        this.fieldHeight = this.fieldWidth;

        this.imageHeight = this.fieldHeight - this.fieldHeight/5;
        this.imageWidth = this.fieldWidth - this.fieldWidth/5;

    }

    @Override
    public void animalMove(EpochStatistic statistic, ChartHandler chartHandler) {
        Platform.runLater(() -> {
            this.gridPaneForWrappedMap.getChildren().clear();
            this.gridPaneForBoundedMap.getChildren().clear();
            this.gridPaneForWrappedMap.getRowConstraints().clear();
            this.gridPaneForBoundedMap.getRowConstraints().clear();
            this.gridPaneForBoundedMap.getColumnConstraints().clear();
            this.gridPaneForWrappedMap.getColumnConstraints().clear();
            showResult(statistic, chartHandler);});
    }

    public void drawGrid() {
        this.gridPaneContainer.setGridLinesVisible(false);
        this.gridPaneForWrappedMap.setGridLinesVisible(false);
        this.gridPaneForBoundedMap.setGridLinesVisible(false);
        this.gridPaneForWrappedMap.setGridLinesVisible(true);
        this.gridPaneForBoundedMap.setGridLinesVisible(true);

        int i = 0;
        for (int x = 0; x < this.mapWidth; x++) {

            int j = 0;
            for (int y = 0; y < this.mapHeight; y++) {
                drawObjectOnWrappedMap(new Vector2d(x,y),i,j);
                drawObjectOnBoundedMap(new Vector2d(x,y),i,j);
                j++;
            }
            i++;
        }
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
            GuiElementBox guiElement = new GuiElementBox(object,imageWidth,imageHeight);
            try {
                this.gridPaneForWrappedMap.add(guiElement.getImage(), i, this.mapHeight - j - 1);
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
        }else {
            object = null;
        }

        if(object != null) {
            GuiElementBox guiElement = new GuiElementBox(object,imageWidth,imageHeight);
            try {
                this.gridPaneForBoundedMap.add(guiElement.getImage(), i,
                        this.mapHeight - j - 1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
    public void showResult(EpochStatistic statistic, ChartHandler chartHandler) {
        drawGrid();
        setConstraintsMaps();
        chartHandler.updateCharts(statistic);
    }

    public void setConstraintsMaps() {
        for(int i = 0; i < this.mapHeight; i++){
            this.gridPaneForBoundedMap.getRowConstraints().add(new RowConstraints(this.fieldHeight));
            this.gridPaneForWrappedMap.getRowConstraints().add(new RowConstraints(this.fieldHeight));
        }
        for(int i = 0; i < this.mapWidth; i++){
            this.gridPaneForBoundedMap.getColumnConstraints().add(new ColumnConstraints(this.fieldWidth));
            this.gridPaneForWrappedMap.getColumnConstraints().add(new ColumnConstraints(this.fieldWidth));
        }

        this.gridPaneForWrappedMap.setPadding(new Insets(0,20,0,20));
        this.gridPaneForBoundedMap.setPadding(new Insets(0,20,0,20));
    }

    public void buttonsHandler(){
        this.gridInfoWrappedMap.pause.setOnAction(event -> {
            if (!wrappedMapEngine.isPaused()) {
                this.gridInfoWrappedMap.pause.setText("Resume");
                this.wrappedMapEngine.pause();
            }
            else{
                this.gridInfoWrappedMap.pause.setText("Stop");
                this.wrappedMapEngine.resume();
            }
        });

        this.gridInfoBoundedMap.pause.setOnAction(event -> {
            if (!boundedMapEngine.isPaused()) {
                this.gridInfoBoundedMap.pause.setText("Resume");
                this.boundedMapEngine.pause();
            }
            else{
                this.gridInfoBoundedMap.pause.setText("Stop");
                this.boundedMapEngine.resume();
            }
        });
    }
}
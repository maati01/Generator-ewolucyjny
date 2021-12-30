package agh.ics.generator.gui;


import agh.ics.generator.*;
import agh.ics.generator.interfaces.IAnimalMoveObserver;
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
import java.util.ArrayList;

public class App extends Application implements IAnimalMoveObserver {
    private final GridPane gridPaneContainer = new GridPane();
    private final GridPane gridPaneForWrappedMap = new GridPane();
    private final GridPane gridPaneForBoundedMap = new GridPane();


    private AbstractWorldMap wrappedMap;
    private AbstractWorldMap boundedMap;
    Thread simulationEngineThread;

    int screenWidth = 1500;
    int screenHeight = 700;
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

    Chart chartAllAnimalsWrappedMap;
    Chart chartAllAnimalsBoundedMap;
    Chart chartAllGrassWrappedMap;
    Chart chartAllGrassBoundedMap;
    Chart chartAvgAnimalsEnergyWrappedMap;
    Chart chartAvgAnimalsEnergyBoundedMap;
    Chart chartLifeExpectancyWrappedMap;
    Chart chartLifeExpectancyBoundedMap;
    Chart avgNumberOfChildrenWrappedMap;
    Chart avgNumberOfChildrenBoundedMap;

    ArrayList<Chart> wrappedMapCharts = new ArrayList<>();
    ArrayList<Chart> boundedMapCharts = new ArrayList<>();

    int cnt = 1;

    //TODO
    //przekazywac do silnika dwie mapy
    //problem z rysowaniem obiektów

    @Override
    public void init() throws Exception {
        try {
            createCharts();
            prepareLayout();
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

            this.wrappedMap = new WrappedGrassField(this.jungleRatio,this.mapWidth,this.mapHeight,
                    this.numberOfStartingAnimals,this.startEnergy,this.moveEnergy,this.plantEnergy);
            this.boundedMap = new BoundedGrassField(this.jungleRatio,this.mapWidth,this.mapHeight,
                    this.numberOfStartingAnimals,this.startEnergy,this.moveEnergy,this.plantEnergy);

            SimulationEngine engine = new SimulationEngine(this.wrappedMap,this.boundedMap);
            this.simulationEngineThread = new Thread(engine);
            engine.addObserver(this);

            Scene scene = new Scene(this.gridPaneContainer,1500,700);
            primaryStage.setScene(scene);
            primaryStage.show();
            simulationEngineThread.start();
        });




        Scene scene = new Scene(input.getTilePane(), 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void prepareLayout(){
        MiddleGridPaneInfo gridInfoWrappedMap = new MiddleGridPaneInfo("Wrapped map",this.wrappedMapCharts);
        MiddleGridPaneInfo gridInfoBoundedMap = new MiddleGridPaneInfo("Bounded map",this.boundedMapCharts);
        LowerGridPaneInfo gridLowerInfoWrappedMap = new LowerGridPaneInfo(this.wrappedMapCharts);
        LowerGridPaneInfo gridLowerInfoBoundedMap = new LowerGridPaneInfo(this.boundedMapCharts);

        this.gridPaneContainer.add(gridInfoWrappedMap,1,0);
        this.gridPaneContainer.add(gridInfoBoundedMap,3,0);
        this.gridPaneContainer.add(gridLowerInfoWrappedMap,0,1);
        this.gridPaneContainer.add(gridLowerInfoBoundedMap,2,1);

        this.gridPaneContainer.add(this.gridPaneForWrappedMap,0,0,1,1);
        this.gridPaneContainer.add(this.gridPaneForBoundedMap,2,0,1,1);
    }

    public void createCharts(){
        this.wrappedMapCharts.add(this.chartAllAnimalsWrappedMap = new Chart("Number of animals"));
        this.boundedMapCharts.add(this.chartAllAnimalsBoundedMap = new Chart("Number of animals"));
        this.wrappedMapCharts.add(this.chartAllGrassWrappedMap = new Chart("Number of grass"));
        this.boundedMapCharts.add(this.chartAllGrassBoundedMap = new Chart("Number of grass"));
        this.wrappedMapCharts.add(this.chartAvgAnimalsEnergyWrappedMap = new Chart("Average energy level"));
        this.boundedMapCharts.add(this.chartAvgAnimalsEnergyBoundedMap = new Chart("Average energy level"));
        this.wrappedMapCharts.add(this.chartLifeExpectancyWrappedMap = new Chart("Average life length"));
        this.boundedMapCharts.add(this.chartLifeExpectancyBoundedMap = new Chart("Average life length"));
        this.wrappedMapCharts.add(this.avgNumberOfChildrenWrappedMap = new Chart("Average number of children"));
        this.boundedMapCharts.add(this.avgNumberOfChildrenBoundedMap = new Chart("Average number of children"));
    }


    public void updateCharts(){
        cnt += 1;
        this.gridPaneContainer.add(this.chartAllAnimalsWrappedMap.updateChart(cnt,cnt),0,1);
        this.gridPaneContainer.add(this.chartAllGrassWrappedMap.updateChart(cnt,cnt),1,1);
        this.gridPaneContainer.add(this.chartAvgAnimalsEnergyWrappedMap.updateChart(cnt,cnt),0,2);
        this.gridPaneContainer.add(this.chartLifeExpectancyWrappedMap.updateChart(cnt,cnt),1,2);
//        this.gridPaneContainer.add(this.avgNumberOfChildrenWrappedMap.updateChart(cnt,cnt),0,15);

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
    public void animalMove() {
        Platform.runLater(() -> {
//            this.gridPaneContainer.getChildren().clear();
            this.gridPaneForWrappedMap.getChildren().clear();
            this.gridPaneForBoundedMap.getChildren().clear();
//            this.gridPaneContainer.getRowConstraints().clear();
            this.gridPaneForWrappedMap.getRowConstraints().clear();
            this.gridPaneForBoundedMap.getRowConstraints().clear();
//            this.gridPaneContainer.getColumnConstraints().clear();
            this.gridPaneForBoundedMap.getColumnConstraints().clear();
            this.gridPaneForWrappedMap.getColumnConstraints().clear();
            showResult();});
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
            GuiElementBox guiElement = new GuiElementBox(object,this.imageWidth,this.imageHeight);
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
        }else{
            object = null;
        }
        if(object != null) {
            GuiElementBox guiElement = new GuiElementBox(object,this.imageWidth,this.imageHeight);
            try {
                this.gridPaneForBoundedMap.add(guiElement.getImage(), i,
                        this.mapHeight - j - 1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    //TODO nie aktualizowac grida kontenera
    public void showResult() {
        drawGrid();
        setConstraintsMaps();

//        updateCharts();

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


}

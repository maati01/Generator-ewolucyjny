package agh.ics.generator.simulation;

import agh.ics.generator.gui.ChartHandler;
import agh.ics.generator.mapelements.animal.Animal;
import agh.ics.generator.maps.AbstractWorldMap;

import java.util.OptionalDouble;

public class EpochStatistic {
    private final AbstractWorldMap map;
    private int day = 1;
    private int allAnimalsMap = 0;
    private int allGrassMap = 0;
    private OptionalDouble avgAnimalsEnergyMap = OptionalDouble.of(0);
    private OptionalDouble lifeExpectancyMap = OptionalDouble.of(0);
    private OptionalDouble numberOfChildrenMap = OptionalDouble.of(0);
    ChartHandler chartHandler;

    public EpochStatistic(AbstractWorldMap map){
        this.map = map;
        this.chartHandler = new ChartHandler(this.map);
//        this.boundedMapChartHandler = new ChartHandler(this.wrappedMap);
    }

    public void updateStatistic(){
        this.day += 1;
        this.allAnimalsMap = this.map.getAnimalsOnMapList().size();
        this.allGrassMap = this.map.getGrassOnMap().size();
        this.avgAnimalsEnergyMap = this.map.getAnimalsOnMapList().stream().mapToDouble(Animal::getEnergy).average();
        this.lifeExpectancyMap = this.map.getAnimalsOnMapList().stream().mapToDouble(Animal::getDayOfLife).average();
        this.numberOfChildrenMap = this.map.getAnimalsOnMapList().stream().mapToDouble(Animal::getNumberOfChildren).average();
    }

    public int getDay(){
        return this.day;
    }

    public int getAllAnimalsMap(){
        return this.allAnimalsMap;
    }

    public int getAllGrassMap(){
        return this.allGrassMap;
    }

    public OptionalDouble getAvgAnimalsEnergyMap(){
        return this.avgAnimalsEnergyMap;
    }

    public OptionalDouble getLifeExpectancyMap(){
        return this.lifeExpectancyMap;
    }

    public OptionalDouble getNumberOfChildrenMap(){
        return this.numberOfChildrenMap;
    }
}

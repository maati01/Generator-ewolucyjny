package agh.ics.generator;

import java.util.OptionalDouble;

public class EpochStatistic {
    private final AbstractWorldMap map;
    private int day = 1;
    private int allAnimalsMap = 0;
    private int allGrassMap = 0;
    private OptionalDouble avgAnimalsEnergyMap = OptionalDouble.of(0);
    private OptionalDouble lifeExpectancyMap = OptionalDouble.of(0);
    private OptionalDouble numberOfChildrenMap = OptionalDouble.of(0);

    public EpochStatistic(AbstractWorldMap map){
        this.map = map;
    }

    public void updateStatistic(){
        this.day += 1;
        this.allAnimalsMap = this.map.getAnimalsOnMap().size();
        this.allGrassMap = this.map.grassOnMap.size();
        this.avgAnimalsEnergyMap = this.map.getAnimalsOnMap().stream().mapToDouble(Animal::getEnergy).average();
        this.lifeExpectancyMap = this.map.getAnimalsOnMap().stream().mapToDouble(Animal::getDayOfLife).average();
        this.numberOfChildrenMap = this.map.getAnimalsOnMap().stream().mapToDouble(Animal::getNumberOfChildren).average();
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

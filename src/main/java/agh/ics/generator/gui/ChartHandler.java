package agh.ics.generator.gui;

import agh.ics.generator.maps.AbstractWorldMap;
import agh.ics.generator.simulation.EpochStatistic;

import java.util.ArrayList;

public class ChartHandler {
    AbstractWorldMap map;
    ArrayList<Chart> mapCharts;
    Chart chartAllAnimals;
    Chart chartAllGrass;
    Chart chartAvgAnimalsEnergy;
    Chart chartLifeExpectancy;
    Chart avgNumberOfChildren;

    public ChartHandler(AbstractWorldMap map){
        this.map = map;
        this.mapCharts = new ArrayList<>();
        createCharts();

    }

    public ArrayList<Chart> getMapCharts() {
        return mapCharts;
    }

    public void createCharts(){
        this.mapCharts.add(this.chartAllAnimals = new Chart("Number of animals"));
        this.mapCharts.add(this.chartAllGrass = new Chart("Number of grass"));
        this.mapCharts.add(this.chartAvgAnimalsEnergy = new Chart("Average energy level"));
        this.mapCharts.add(this.chartLifeExpectancy = new Chart("Average life length"));
        this.mapCharts.add(this.avgNumberOfChildren = new Chart("Average number of children"));
    }

    public void updateCharts(EpochStatistic statistic){
        this.mapCharts.get(0).updateChart(statistic.getDay(),statistic.getAllAnimalsMap());
        this.mapCharts.get(1).updateChart(statistic.getDay(),statistic.getAllGrassMap());
        this.mapCharts.get(2).updateChart(statistic.getDay(),statistic.getAvgAnimalsEnergyMap().orElse(-1));
        this.mapCharts.get(3).updateChart(statistic.getDay(),statistic.getLifeExpectancyMap().orElse(-1));
        this.mapCharts.get(4).updateChart(statistic.getDay(),statistic.getNumberOfChildrenMap().orElse(-1));

    }
}

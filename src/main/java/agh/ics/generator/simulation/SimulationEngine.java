package agh.ics.generator.simulation;

import agh.ics.generator.gui.ChartHandler;
import agh.ics.generator.interfaces.IAnimalMoveObserver;
import agh.ics.generator.interfaces.IEngine;
import agh.ics.generator.mapelements.Vector2d;
import agh.ics.generator.mapelements.animal.Animal;
import agh.ics.generator.mapelements.animal.Reproduction;
import agh.ics.generator.maps.AbstractWorldMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SimulationEngine implements IEngine, Runnable{
    private final AbstractWorldMap map;
    private List<Animal> animalsOnMap;
    private final List<IAnimalMoveObserver> observers = new ArrayList<>();
    private final int moveDelay = 200;
    private final Reproduction reproduction;
    private final EpochStatistic epochStatistic;
    public boolean isPaused = false;

    ChartHandler chartHandler;


    public List<Animal> getAnimalsOnMap() {
        return this.animalsOnMap;
    }

    public SimulationEngine(AbstractWorldMap map, ChartHandler chartHandler){
        this.map = map;
        this.animalsOnMap = new ArrayList<>();
        this.reproduction = new Reproduction(this.map);
        this.epochStatistic = new EpochStatistic(this.map);
        this.chartHandler = chartHandler;
    }

    public List<Animal> findAnimalsThatEatGrass(List<Animal> animals){
        List<Animal> foundAnimals = new ArrayList<>();

        Animal firstAnimal = animals.get(0);
        foundAnimals.add(firstAnimal);

        for(int i = 1; i < animals.size(); i++){
            if(animals.get(i).getEnergy() < firstAnimal.getEnergy()){
                break;
            }else{
                foundAnimals.add(animals.get(i));
            }
        }
        return foundAnimals;
    }

    public void pause() {
        this.isPaused = true;
    }

    public void resume() {
        this.isPaused = false;
        synchronized (this) {
            notify();
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    private void pauseIfRequested() {
        synchronized (this) {
            while (this.isPaused) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void eatGrass(AbstractWorldMap map){
        for (Vector2d vector2d: map.getAnimalsOnMap().keySet()){
            map.getAnimalsOnMap().get(vector2d).sort((o1, o2) -> {
                return Integer.compare(o2.getEnergy(),o1.getEnergy());
            });

            if(map.getGrassOnMap().containsKey(vector2d) && !map.getAnimalsOnMap().get(vector2d).isEmpty()){
                List<Animal> animals = findAnimalsThatEatGrass(map.getAnimalsOnMap().get(vector2d));
                int energyPortion = map.getGrassOnMap().get(vector2d).getPlantEnergy()/animals.size();

                for(Animal animal : animals){
                    animal.addEnergy(energyPortion);
                }
                map.getGrassOnMap().remove(vector2d);

            }
        }

    }

    public void removeAnimalsFromMap(){
        for(List<Animal> animals: this.map.getAnimalsOnMap().values()){
            for (Iterator<Animal> i = animals.iterator(); i.hasNext();) {
                Animal animal = i.next();
                if (animal.getEnergy() == 0) {
                    i.remove();
                    this.map.removeAnimalFromMap(animal.getPosition(),animal);
                    this.animalsOnMap.remove(animal);
                }
            }
        }
        map.getAnimalsOnMap().keySet().removeIf(element -> map.getAnimalsOnMap().get(element).isEmpty());
    }

    public void simulationEpoch(){
        this.map.generateGrass();
        eatGrass(this.map);
        removeAnimalsFromMap();
        reproduction.doReproduction(this.map.getAnimalsOnMap());
        this.animalsOnMap = this.map.getAnimalsOnMapList();
        this.epochStatistic.updateStatistic();
    }

    @Override
    public void run() {
        int size;

        for(IAnimalMoveObserver observer : this.observers) {
            observer.animalMove(this.epochStatistic,this.chartHandler);
        }

        while(true){
            size = this.animalsOnMap.size();

            for(int i = 0;i < size; i++){
                this.animalsOnMap.get(i).move();
            }

            simulationEpoch();
            pauseIfRequested();

            for(IAnimalMoveObserver observer : this.observers) {
                observer.animalMove(this.epochStatistic,this.chartHandler);
            }

            try {
                TimeUnit.MILLISECONDS.sleep(this.moveDelay);
            } catch (InterruptedException e) {
            }

        }

    }

    public void addObserver(IAnimalMoveObserver observer){
        this.observers.add(observer);
    }

    public void removeObserver(IAnimalMoveObserver observer){
        this.observers.remove(observer);
    }
}

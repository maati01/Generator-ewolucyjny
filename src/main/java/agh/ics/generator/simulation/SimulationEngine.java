package agh.ics.generator.simulation;

import agh.ics.generator.mapelements.Vector2d;
import agh.ics.generator.mapelements.animal.Animal;
import agh.ics.generator.mapelements.animal.Reproduction;
import agh.ics.generator.maps.AbstractWorldMap;
import agh.ics.generator.maps.WrappedGrassField;
import agh.ics.generator.interfaces.IAnimalMoveObserver;
import agh.ics.generator.interfaces.IEngine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SimulationEngine implements IEngine, Runnable{
    private final AbstractWorldMap wrappedMap;
    private final AbstractWorldMap boundedMap;
    private List<Animal> animalsOnWrappedMap;
    private List<Animal> animalsOnBoundedMap;
    private final List<IAnimalMoveObserver> observers = new ArrayList<>();
    private final int moveDelay = 200;
    private final Reproduction reproductionAnimalsOnWrappedMap;
    private final Reproduction reproductionAnimalsOnBoundedMap;
    private final EpochStatistic epochStatisticWrappedMap;
    private final EpochStatistic epochStatisticBoundedMap;


    public List<Animal> getAnimalsOnWrappedMap() {
        return this.animalsOnWrappedMap;
    }

    public List<Animal> getAnimalsOnBoundedMap(){ return this.animalsOnBoundedMap;}


    public SimulationEngine(AbstractWorldMap wrappedMap,AbstractWorldMap boundedMap){
        this.wrappedMap = wrappedMap;
        this.boundedMap = boundedMap;
        this.animalsOnWrappedMap = new ArrayList<>();
        this.animalsOnBoundedMap = new ArrayList<>();
        this.reproductionAnimalsOnBoundedMap = new Reproduction(this.boundedMap);
        this.reproductionAnimalsOnWrappedMap = new Reproduction(this.wrappedMap);
        this.epochStatisticWrappedMap = new EpochStatistic(this.wrappedMap);
        this.epochStatisticBoundedMap = new EpochStatistic(this.boundedMap);
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

    public void removeAnimalsFromMap(AbstractWorldMap map){
        for(List<Animal> animals: map.getAnimalsOnMap().values()){
            for (Iterator<Animal> i = animals.iterator(); i.hasNext();) {
                Animal animal = i.next();
                if (animal.getEnergy() == 0) {
                    i.remove();
                    if(map instanceof WrappedGrassField){
                        this.wrappedMap.removeAnimalFromMap(animal.getPosition(),animal);
                        this.animalsOnWrappedMap.remove(animal);

                    }else{
                        this.boundedMap.removeAnimalFromMap(animal.getPosition(), animal);
                        this.animalsOnBoundedMap.remove(animal);
                    }

                }
            }
        }
        map.getAnimalsOnMap().keySet().removeIf(element -> map.getAnimalsOnMap().get(element).isEmpty());
    }

    public void simulationEpoch(){
        this.boundedMap.generateGrass();
        this.wrappedMap.generateGrass();
        eatGrass(this.boundedMap);
        eatGrass(this.wrappedMap);
        removeAnimalsFromMap(this.boundedMap);
        removeAnimalsFromMap(this.wrappedMap);
        reproductionAnimalsOnWrappedMap.doReproduction(this.wrappedMap.getAnimalsOnMap());
        reproductionAnimalsOnBoundedMap.doReproduction(this.boundedMap.getAnimalsOnMap());
        this.animalsOnWrappedMap = this.wrappedMap.getAnimalsOnMapList();
        this.animalsOnBoundedMap = this.boundedMap.getAnimalsOnMapList();
        this.epochStatisticBoundedMap.updateStatistic();
        this.epochStatisticWrappedMap.updateStatistic();

    }

    @Override
    public void run() {
        int size1;
        int size2;

        for(IAnimalMoveObserver observer : observers) {
            observer.animalMove(this.epochStatisticWrappedMap,this.epochStatisticBoundedMap);
        }


        while(true){
            size1 = this.animalsOnWrappedMap.size();
            size2 = this.animalsOnBoundedMap.size();

            for(int i = 0;i < size1; i++){
                this.animalsOnWrappedMap.get(i).move();
            }

            for(int i = 0;i < size2; i++){
                this.animalsOnBoundedMap.get(i).move();
            }
            simulationEpoch();

            for(IAnimalMoveObserver observer : this.observers) {
                observer.animalMove(this.epochStatisticWrappedMap,this.epochStatisticBoundedMap);
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


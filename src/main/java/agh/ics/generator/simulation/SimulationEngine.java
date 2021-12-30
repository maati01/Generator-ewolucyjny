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

//    public SimulationEngine(List<MoveDirection> moves,AbstractWorldMap map, List<Vector2d> positions){
//        this.moves = moves;
//        this.map = map;
//        this.animals = new ArrayList<>();
//
//        for(Vector2d vector2d: positions){
//            Animal animal = new Animal(this.map,vector2d,100);
//            if(map.place(animal)){
//                animal.addObserver(map);
//                this.animals.add(animal);
//
//            }
//        }
//    }

    public SimulationEngine(AbstractWorldMap wrappedMap,AbstractWorldMap boundedMap){
        this.wrappedMap = wrappedMap;
        this.boundedMap = boundedMap;
        this.animalsOnWrappedMap = new ArrayList<>();
        this.animalsOnBoundedMap = new ArrayList<>();
        this.reproductionAnimalsOnBoundedMap = new Reproduction(this.boundedMap);
        this.reproductionAnimalsOnWrappedMap = new Reproduction(this.wrappedMap);
        this.epochStatisticWrappedMap = new EpochStatistic(this.wrappedMap);
        this.epochStatisticBoundedMap = new EpochStatistic(this.boundedMap);


//        for(Vector2d vector2d: positions){
//            Animal animalOnWrappedMap = new Animal(this.wrappedMap,vector2d,10);
//            Animal animalOnBoundedMap = new Animal(this.boundedMap,vector2d,10);
//            this.wrappedMap.place(animalOnWrappedMap);
//            this.animalsOnWrappedMap.add(animalOnWrappedMap);
//            this.boundedMap.place(animalOnBoundedMap);
//            this.animalsOnBoundedMap.add(animalOnBoundedMap);
//            }

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
                        this.animalsOnWrappedMap.remove(animal);
                        if(this.wrappedMap.getPossibleJunglePositions().contains(animal.getPosition())){
                            this.wrappedMap.getPossibleJunglePositions().remove(animal.getPosition());
                        }else{
                            this.wrappedMap.getPossibleStepPositions().remove(animal.getPosition());
                        }
                    }else{
                        this.animalsOnBoundedMap.remove(animal);
                        if(this.boundedMap.getPossibleJunglePositions().contains(animal.getPosition())){
                            this.boundedMap.getPossibleJunglePositions().remove(animal.getPosition());
                        }else{
                            this.boundedMap.getPossibleStepPositions().remove(animal.getPosition());
                        }
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
                System.out.println("Simulation has been aborted");
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


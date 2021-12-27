package agh.ics.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SimulationEngine implements IEngine, Runnable{
    protected List<MoveDirection> moves;
    private final AbstractWorldMap wrappedMap;
    private final AbstractWorldMap boundedMap;
    private final List<Animal> animalsOnWrappedMap;
    private final List<Animal> animalsOnBoundedMap;
    private final List<IAnimalMoveObserver> observers = new ArrayList<>();
    private final int moveDelay = 300;


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

    public SimulationEngine(AbstractWorldMap wrappedMap,AbstractWorldMap boundedMap, List<Vector2d> positions){
        this.wrappedMap = wrappedMap;
        this.boundedMap = boundedMap;
        this.animalsOnWrappedMap = new ArrayList<>();
        this.animalsOnBoundedMap = new ArrayList<>();

        for(Vector2d vector2d: positions){
            Animal animalOnWrappedMap = new Animal(this.wrappedMap,vector2d,100);
            Animal animalOnBoundedMap = new Animal(this.boundedMap,vector2d,100);
            animalOnWrappedMap.addObserver(this.wrappedMap);
            this.wrappedMap.place(animalOnWrappedMap);
            this.animalsOnWrappedMap.add(animalOnWrappedMap);
            animalOnBoundedMap.addObserver(this.boundedMap);
            this.boundedMap.place(animalOnBoundedMap);
            this.animalsOnBoundedMap.add(animalOnBoundedMap);
            }
        }

    public void simulationEpoch(){
        this.boundedMap.generateGrass();
        this.wrappedMap.generateGrass();
    }

    @Override
    public void run() {
        int size1 = this.animalsOnWrappedMap.size();
        int size2 = this.animalsOnBoundedMap.size();

        simulationEpoch();
        for(IAnimalMoveObserver observer : observers) {
            observer.animalMove();
        }

        while(true){
            simulationEpoch();
            for(int i = 0;i < size2; i++){
                this.animalsOnBoundedMap.get(i).move();
                this.animalsOnWrappedMap.get(i).move();
            }

            for(IAnimalMoveObserver observer : this.observers) {
                observer.animalMove();
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


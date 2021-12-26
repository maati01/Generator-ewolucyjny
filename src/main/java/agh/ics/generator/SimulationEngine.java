package agh.ics.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SimulationEngine implements IEngine, Runnable{
    protected List<MoveDirection> moves;
    private final AbstractWorldMap map;
    private final List<Animal> animals;
    private final List<IAnimalMoveObserver> observers = new ArrayList<>();
    private final int moveDelay = 32000;


    public List<Animal> getAnimals() {
        return animals;
    }

    public SimulationEngine(List<MoveDirection> moves,AbstractWorldMap map, List<Vector2d> positions){
        this.moves = moves;
        this.map = map;
        this.animals = new ArrayList<>();

        for(Vector2d vector2d: positions){
            Animal animal = new Animal(this.map,vector2d,100);
            if(map.place(animal)){
                animal.addObserver(map);
                this.animals.add(animal);

            }
        }
    }

    public SimulationEngine(AbstractWorldMap map, List<Vector2d> positions){
        this.map = map;
        this.animals = new ArrayList<>();

        for(Vector2d vector2d: positions){
            Animal animal = new Animal(this.map,vector2d,100);
            if(map.place(animal)){
                animal.addObserver(map);
                this.animals.add(animal);

            }
        }
    }

    @Override
    public void run() {
        int size = this.animals.size();
        for(IAnimalMoveObserver observer : observers) {
            observer.animalMove();
        }
        while(true){
            for(int i = 0;i < size; i++){
                this.animals.get(i).move();
            }
            for(IAnimalMoveObserver observer : this.observers) {
                observer.animalMove();
            }
            System.out.println(this.map.elementsOnMap);
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


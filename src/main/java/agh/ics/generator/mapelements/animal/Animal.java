package agh.ics.generator.mapelements.animal;

import agh.ics.generator.enums.MapDirection;
import agh.ics.generator.mapelements.AbstractWorldMapElement;
import agh.ics.generator.mapelements.Vector2d;
import agh.ics.generator.maps.AbstractWorldMap;
import agh.ics.generator.maps.WrappedGrassField;
import agh.ics.generator.interfaces.IPositionChangeObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.Math.max;

public class Animal extends AbstractWorldMapElement {
    private MapDirection vector;
    private Vector2d position;
    private AbstractWorldMap map;
    private final List<IPositionChangeObserver> observers = new ArrayList<>();
    private List<Integer> genes;
    private int energy;
    private int dayOfLife = 1;
    private int numberOfChildren = 0;
    Random random = new Random();


    //TODO
    //sprawdzac gdzie dodawac pozycje po position change
    //uzyc tutaj klasy genotyp

    public Animal(){

    }

    //konstruktor uzywany jest przy repordukcji
    public Animal(AbstractWorldMap map, int energy, Genotype genotype, Vector2d position) {
        this.map = map;
        this.energy = energy;
        this.genes = genotype.getGenotype();
        this.position = position;
        this.vector = getRandomDirection();
        this.addObserver(this.map);
    }


    //konstruktor przy początkowych zwierzętach
    public Animal(AbstractWorldMap map,Vector2d position, int startEnergy){
        this.map = map;
        this.position = position;
        this.energy = startEnergy;
        this.genes =  random.ints(32, 0, 7).boxed().sorted()
                .collect(Collectors.toList());
        this.vector = getRandomDirection();
        this.addObserver(this.map);
    }

    public int getDayOfLife(){
        return this.dayOfLife;
    }

    public int getNumberOfChildren(){
        return this.numberOfChildren;
    }

    public void updateNumberOfChildren() {
        this.numberOfChildren += 1;
    }

    public int getEnergy(){
        return this.energy;
    }

    public void setEnergy(int energy){
        this.energy = energy;
    }
    public List<Integer> getGenes(){
        return this.genes;
    }

    public MapDirection getOrientation() {
        return vector;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String getImagePath() {
        return "src/main/resources/rabbit_east.png";
    }

    @Override
    public String toStringInGui() {
        return null;
    }

//    @Override
//    public String getImagePath() {
//        return switch (this.vector){
//            case NORTH -> "src/main/resources/rabbit_north.png";
//            case SOUTH -> "src/main/resources/rabbit_south.png";
//            case WEST -> "src/main/resources/rabbit_west.png";
//            case EAST -> "src/main/resources/rabbit_east.png";
//        };
//    }

    public MapDirection getRandomDirection(){
        return directionForIndex(random.nextInt(8));
    }

    //TODO to chyba zbedne jest
    public MapDirection directionForIndex(int index){
        return switch (index){
            case 0 -> MapDirection.NORTH;
            case 1 -> MapDirection.NORTH_EAST;
            case 2 -> MapDirection.EAST;
            case 3 -> MapDirection.SOUTH_EAST;
            case 4 -> MapDirection.SOUTH;
            case 5 -> MapDirection.SOUTH_WEST;
            case 6 -> MapDirection.WEST;
            case 7 -> MapDirection.NORTH_WEST;
            default -> throw new IllegalStateException("Unexpected value: " + index);
        };
    }

    public Vector2d wrappedNewPosition(Vector2d vector2d){
        int minX = 0;
        int minY = 0;
        int maxX = this.map.getWidth();
        int maxY = this.map.getHeight();
        int x = vector2d.x;
        int y = vector2d.y;
        if(x < minX){
            x = maxX;
        }
        if(y < minY){
            y = maxY;
        }
        if(x > maxX){
            x = minX;
        }
        if(y > maxY){
            y = minY;
        }
        return new Vector2d(x,y);
    }

    public Vector2d boundedNewPosition(Vector2d vector2d){
        int x = vector2d.x;
        int y = vector2d.y;
        int minX = 0;
        int minY = 0;
        int maxX = this.map.getWidth();
        int maxY = this.map.getHeight();

        if(x < minX){
            x = minX;
        }
        if(y < minY) {
            y = minY;
        }
        if(x > maxX) {
            x = maxX;
        }
        if(y > maxY){
            y = maxY;
        }
        return new Vector2d(x,y);
    }

    public void addEnergy(int energyPortion){
        this.energy += energyPortion;
    }

    public void updateEnergy(){
        this.energy = max(this.energy-this.map.getMoveEnergy(),0);
    }

    public int pickMove(){
        return this.genes.get(random.nextInt(32));
    }

    public void move(){
        int move = pickMove();
        Vector2d newPosition;
        this.dayOfLife += 1;
        switch (move){
            case 0 -> {
                if(this.map instanceof WrappedGrassField) {
                    newPosition = wrappedNewPosition(this.position.add(this.vector.toUnitVector()));
                }
                else{
                    newPosition = boundedNewPosition(this.position.add(this.vector.toUnitVector()));
                }

                positionChanged(this.position, newPosition,this);

                this.position = newPosition;


            }
            case 4 -> {
                if(this.map instanceof WrappedGrassField) {
                    newPosition = wrappedNewPosition(this.position.add(this.vector.toUnitVector().opposite()));
                }
                else{
                    newPosition = boundedNewPosition(this.position.add(this.vector.toUnitVector().opposite()));
                }
                positionChanged(this.position, newPosition,this);

                this.position = newPosition;

            }
            default -> this.vector = this.vector.directionForIndex((this.vector.indexForDirection() + move)%7); //TODO to jest podejrzane

        }
        updateEnergy();
    }

    void addObserver(IPositionChangeObserver observer){
        this.observers.add(observer);
    }

    void removeObserver(IPositionChangeObserver observer){
        this.observers.remove(observer);
    }

    //TODO
    //w tej petli trzeba raczej zrobic funkcje

    void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal){
        for (IPositionChangeObserver observer : this.observers){
            observer.positionChanged(oldPosition, newPosition, animal);

            if(this.map.getElementsOnMap().containsKey(oldPosition) && this.map.getElementsOnMap().get(oldPosition).size() > 1) {
                if (oldPosition.x >= this.map.getJungleLowerLeft().x && oldPosition.x <= this.map.getJungleUpperRight().x
                        && oldPosition.y >= this.map.getJungleLowerLeft().y && oldPosition.y <= this.map.getJungleUpperRight().y) {
                    this.map.getPossibleJunglePositions().add(new Vector2d(oldPosition.x, oldPosition.y));
                } else {
                    this.map.getPossibleStepPositions().add(new Vector2d(oldPosition.x, oldPosition.y));
                }
            }
            if(newPosition.x >= this.map.getJungleLowerLeft().x && newPosition.x <= this.map.getJungleUpperRight().x
                && newPosition.y >= this.map.getJungleLowerLeft().y && newPosition.y <= this.map.getJungleUpperRight().y){
                this.map.getPossibleJunglePositions().remove(new Vector2d(newPosition.x,newPosition.y));
            }
            else{
                this.map.getPossibleStepPositions().remove(new Vector2d(newPosition.x,newPosition.y));
            }
    }
}}

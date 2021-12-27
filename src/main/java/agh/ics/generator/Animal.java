package agh.ics.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Animal extends AbstractWorldMapElement {
    private MapDirection vector = MapDirection.NORTH;
    private Vector2d position = new Vector2d(2,2);
    private AbstractWorldMap map;
    private final List<IPositionChangeObserver> observers = new ArrayList<>();
    private int energy;
    private List<Integer> genes;
    private int dayCounter = 0;
    Random random = new Random();


    //TODO
    //sprawdzac gdzie dodawac pozycje po position change

    public Animal(AbstractWorldMap map, int energy) {
        this.map = map;
        this.energy = energy;
        this.genes =  random.ints(32, 0, 7).boxed().sorted()
                .collect(Collectors.toList());
        this.addObserver(this.map);
    }

    public Animal(AbstractWorldMap map, Vector2d initialPosition, int energy){
        this.map = map;
        this.position = initialPosition;
        this.energy = energy;
        this.genes =  random.ints(32, 0, 7).boxed().sorted()
                .collect(Collectors.toList());
        this.addObserver(this.map);
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
    public Vector2d wrappedNewPosition(Vector2d vector2d){
        int minX = this.map.getLowerLeft().x;
        int minY = this.map.getLowerLeft().y;
        int maxX = this.map.getUpperRight().x;
        int maxY = this.map.getUpperRight().y;
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
        int minX = this.map.getLowerLeft().x;
        int minY = this.map.getLowerLeft().y;
        int maxX = this.map.getUpperRight().x;
        int maxY = this.map.getUpperRight().y;

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

    public void move(){
        int move = (genes.get(dayCounter) + this.vector.indexForDirection())%7;
        Vector2d newPosition;

        switch (genes.get(dayCounter)){
            case 0 -> {
                if(this.map instanceof WrappedGrassField) {
                    newPosition = wrappedNewPosition(this.position.add(this.vector.toUnitVector()));
                }
                else{
                    newPosition = boundedNewPosition(this.position.add(this.vector.toUnitVector()));
                }

                positionChanged(this.position, newPosition);

                this.position = newPosition;

            }
            case 4 -> {
                if(this.map instanceof WrappedGrassField) {
                    newPosition = wrappedNewPosition(this.position.add(this.vector.toUnitVector().opposite()));
                }
                else{
                    newPosition = boundedNewPosition(this.position.add(this.vector.toUnitVector().opposite()));
                }
                positionChanged(this.position, newPosition);


                this.position = newPosition;
            }
            default -> this.vector = this.vector.directionForIndex(move);
        }
        dayCounter += 1;
        if(dayCounter == 31){
            dayCounter = 0;
        }
    }

    void addObserver(IPositionChangeObserver observer){
        this.observers.add(observer);
    }

    void removeObserver(IPositionChangeObserver observer){
        this.observers.remove(observer);
    }

    //TODO
    //w tej petli trzeba raczej zrobic funkcje

    void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for (IPositionChangeObserver observer : this.observers)
            observer.positionChanged(oldPosition, newPosition);

            if(oldPosition.x >= this.map.jungleLowerLeft.x && oldPosition.x <= this.map.jungleUpperRight.x
                    && oldPosition.y >= this.map.jungleLowerLeft.y && oldPosition.y <= this.map.jungleUpperRight.y){
                this.map.possibleJunglePositions.add(new Vector2d(oldPosition.x,oldPosition.y));
            }
            else{
                this.map.possibleStepPositions.add(new Vector2d(oldPosition.x,oldPosition.y));
            }
            if(newPosition.x >= this.map.jungleLowerLeft.x && newPosition.x <= this.map.jungleUpperRight.x
                && newPosition.y >= this.map.jungleLowerLeft.y && newPosition.y <= this.map.jungleUpperRight.y){
                this.map.possibleJunglePositions.remove(new Vector2d(newPosition.x,newPosition.y));
            }
            else{
                this.map.possibleStepPositions.remove(new Vector2d(newPosition.x,newPosition.y));
            }
    }
}

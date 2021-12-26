package agh.ics.generator;

import java.util.HashMap;
import java.util.Random;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{
    protected HashMap<Vector2d, AbstractWorldMapElement> elementsOnMap = new HashMap<>();
//    protected Vector2d lowerLeft = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
//    protected Vector2d upperRight = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
    protected Vector2d lowerLeft = new Vector2d(0, 0);
    protected Vector2d upperRight = new Vector2d(10, 10);
    protected int numberGrasses;

    @Override
    public boolean canMoveTo(Vector2d position) {
        AbstractWorldMapElement object = objectAt(position);
        if (object instanceof Grass) {
            this.elementsOnMap.remove(position);
            generateGrass(1);
        }
        return true;
    }

    @Override
    public boolean place(Animal animal) {
        if(canMoveTo(animal.getPosition())){
            this.elementsOnMap.put(animal.getPosition(),animal);
            return true;
        }
        throw new IllegalArgumentException("Position " + animal.getPosition() + " is wrong. Here is another animal.");
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public AbstractWorldMapElement objectAt(Vector2d position) {
        if (this.elementsOnMap.containsKey(position)) {
            return this.elementsOnMap.get(position);
        }
        return null;
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        AbstractWorldMapElement object = objectAt(oldPosition);
        if (object instanceof Animal) {
            this.elementsOnMap.put(newPosition, object);
            this.elementsOnMap.remove(oldPosition, object);
        }
    }

    public void generateGrass(int quantity){
        Random rand = new Random();
        int cnt = 0;
        while (cnt < quantity){
            int x = rand.nextInt((int) Math.sqrt(this.numberGrasses*10));
            int y = rand.nextInt((int) Math.sqrt(this.numberGrasses*10));

            Vector2d new_position = new Vector2d(x,y);
            if (!isOccupied(new_position)) {
                cnt++;
                this.elementsOnMap.put(new_position, new Grass(new_position));
            }
        }
    }

    public Vector2d getLowerLeft(){
        return this.lowerLeft;
    }

    public Vector2d getUpperRight(){
        return this.upperRight;
    }
}


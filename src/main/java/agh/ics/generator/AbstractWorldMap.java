package agh.ics.generator;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Random;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{
    protected final LinkedHashMap<Vector2d, AbstractWorldMapElement> elementsOnMap = new LinkedHashMap<>();
    protected Vector2d lowerLeft = new Vector2d(0, 0);
    protected Vector2d upperRight = new Vector2d(10, 10);
    protected int numberGrasses = 1;
    protected double jungleRatio;
    protected HashSet<Vector2d> possibleStepPositions = new HashSet<>();
    protected HashSet<Vector2d> possibleJunglePositions = new HashSet<>();
    int rangeX = upperRight.x - lowerLeft.x;
    int rangeY = upperRight.y - lowerLeft.y;
    Vector2d jungleLowerLeft;
    Vector2d jungleUpperRight;

    //TODO
    //zmienic nazwe na steppe

    public AbstractWorldMap(double jungleRatio){
        this.jungleRatio = jungleRatio;
        findJungleCorners();
        findStepAndJunglePositions();
    }

    public void findJungleCorners(){
        int a = lowerLeft.x + (int) Math.floor(Math.sqrt(jungleRatio)*rangeX);
        int b = lowerLeft.y + (int) Math.floor(Math.sqrt(jungleRatio)*rangeY);

        this.jungleLowerLeft = new Vector2d((rangeX - a)/2,(rangeY - b)/2);
        this.jungleUpperRight = new Vector2d(this.jungleLowerLeft.x + a,b + this.jungleLowerLeft.y);
    }

    public void addPositions(int i, int j){
        if(i >= jungleLowerLeft.x && i <= jungleUpperRight.x && j >= jungleLowerLeft.y && j <= jungleUpperRight.y){
            this.possibleJunglePositions.add(new Vector2d(i,j));
        }
        else{
            this.possibleStepPositions.add(new Vector2d(i,j));
        }
    }

    public boolean checkPointInJungle(int i, int j){
        return i >= jungleLowerLeft.x && i <= jungleUpperRight.x && j >= jungleLowerLeft.y && j <= jungleUpperRight.y;
    }

    public void findStepAndJunglePositions(){
        for(int i = 0; i <= rangeX; i++){
            for(int j = 0; j <= rangeY; j++){
                addPositions(i,j);
            }
        }
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        AbstractWorldMapElement object = objectAt(position);
        if (object instanceof Grass) {
            this.elementsOnMap.remove(position);
        }
        return true;
    }

    @Override
    public boolean place(Animal animal) {
        if(canMoveTo(animal.getPosition())){
            this.elementsOnMap.put(animal.getPosition(),animal);
            if (checkPointInJungle(animal.getPosition().x,animal.getPosition().y))
            {
                this.possibleJunglePositions.remove(animal.getPosition());
            }
            else{
                this.possibleStepPositions.remove(animal.getPosition());
            }


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
        return this.elementsOnMap.get(position);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        AbstractWorldMapElement object = objectAt(oldPosition);
        if (object instanceof Animal) {
            this.elementsOnMap.remove(oldPosition, object);
            this.elementsOnMap.put(newPosition, object);
        }
    }

    public Vector2d getRandom(HashSet<Vector2d> hashSet) {
        Vector2d[] arrayNumbers = hashSet.toArray(new Vector2d[0]);
        int rnd = new Random().nextInt(hashSet.size());
        return arrayNumbers[rnd];
    }

    public void generateGrass(){
        if (this.possibleJunglePositions.size() > 0) {
            Vector2d jungleGrassPosition = getRandom(this.possibleJunglePositions);
            this.elementsOnMap.put(jungleGrassPosition,new Grass(jungleGrassPosition));
            this.possibleJunglePositions.remove(jungleGrassPosition);
        }

        if(this.possibleStepPositions.size() > 0){
            Vector2d stepGrassPosition = getRandom(this.possibleStepPositions);
            this.elementsOnMap.put(stepGrassPosition,new Grass(stepGrassPosition));
            this.possibleStepPositions.remove(stepGrassPosition);
        }
    }

    public Vector2d getLowerLeft(){
        return this.lowerLeft;
    }

    public Vector2d getUpperRight(){
        return this.upperRight;
    }
}


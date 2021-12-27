package agh.ics.generator;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver{
    protected final HashMap<Vector2d, List<AbstractWorldMapElement>> elementsOnMap = new HashMap<>();
    protected Vector2d lowerLeft = new Vector2d(0, 0);
    protected Vector2d upperRight = new Vector2d(3, 3);
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

            if(this.elementsOnMap.containsKey(animal.getPosition())){
                this.elementsOnMap.get(animal.getPosition()).add(animal);
            }else{
                this.elementsOnMap.put(animal.getPosition(), new ArrayList<>(List.of(animal)));
            }


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
        if(this.elementsOnMap.get(position) != null){
            return this.elementsOnMap.get(position).get(0);
        }else{
            return null;
        }
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition,Animal animal) {
        AbstractWorldMapElement object = objectAt(oldPosition);

        if(canMoveTo(newPosition)) {
            if (object instanceof Animal) {
                System.out.println();
                if(this.elementsOnMap.containsKey(oldPosition) && this.elementsOnMap.get(oldPosition).size() == 1){
                    this.elementsOnMap.remove(oldPosition);
                }else{
                    this.elementsOnMap.get(oldPosition).remove(animal);
                }

                if (this.elementsOnMap.containsKey(newPosition)) {
                    this.elementsOnMap.get(newPosition).add(animal);
                }
                else {
                    this.elementsOnMap.put(newPosition, new ArrayList<>(List.of(animal)));
                }


            }
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

            if(this.elementsOnMap.containsKey(jungleGrassPosition)){
                this.elementsOnMap.get(jungleGrassPosition).add(new Grass(jungleGrassPosition));
            }else{
                this.elementsOnMap.put(jungleGrassPosition, Collections.singletonList(new Grass(jungleGrassPosition)));
            }

            this.possibleJunglePositions.remove(jungleGrassPosition);
        }

        if(this.possibleStepPositions.size() > 0){
            Vector2d stepGrassPosition = getRandom(this.possibleStepPositions);

            if(this.elementsOnMap.containsKey(stepGrassPosition)){
                this.elementsOnMap.get(stepGrassPosition).add(new Grass(stepGrassPosition));
            }else{
                this.elementsOnMap.put(stepGrassPosition, Collections.singletonList(new Grass(stepGrassPosition)));
            }

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


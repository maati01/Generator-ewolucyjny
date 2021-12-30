package agh.ics.generator;

import agh.ics.generator.interfaces.IPositionChangeObserver;
import agh.ics.generator.interfaces.IWorldMap;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected final HashMap<Vector2d, List<AbstractWorldMapElement>> elementsOnMap = new HashMap<>();
    protected final HashMap<Vector2d, List<Animal>> animalsOnMap = new HashMap<>();
    protected final HashMap<Vector2d, Grass> grassOnMap = new HashMap<>();
    protected int startEnergy;
    //    protected Vector2d lowerLeft = new Vector2d(0, 0);
//    protected Vector2d upperRight;
    protected int width;
    protected int height;
    protected double jungleRatio;
    protected int moveEnergy;
    protected int plantEnergy;;
    protected HashSet<Vector2d> possibleStepPositions = new HashSet<>();
    protected HashSet<Vector2d> possibleJunglePositions = new HashSet<>();
//    protected HashSet<Vector2d> allPossiblePositions = new HashSet<>();
//    int rangeX;
//    int rangeY;
    Vector2d jungleLowerLeft;
    Vector2d jungleUpperRight;

    //TODO
    //zmienic nazwe na steppe

    public AbstractWorldMap(double jungleRatio,int width, int height, int numberOfStartingAnimals,int startEnergy,
                            int moveEnergy, int plantEnergy){
        this.jungleRatio = jungleRatio;
        this.width = width;
        this.height = height;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.startEnergy = startEnergy;
        findJungleCorners();
        findStepAndJunglePositions();
        generateStartAnimals(numberOfStartingAnimals);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void generateStartAnimals(int numberOfStartingAnimals){
        for(int i = 0; i < numberOfStartingAnimals; i++){ //TODO to musi byc w funkcji XD
            if(this.possibleJunglePositions.size() < 1){
                return;
            }
            Vector2d newAnimaPosition = getRandom(this.possibleJunglePositions);
            this.place(new Animal(this,newAnimaPosition,this.startEnergy));
//            if(this.elementsOnMap.containsKey(newAnimaPosition)){
//                this.elementsOnMap.get(newAnimaPosition).add(new Animal(newAnimaPosition));
//            }else{
//                this.elementsOnMap.put(newAnimaPosition, Collections.singletonList(new Animal(newAnimaPosition)));
//
//            }
//            this.animalsOnMap.put(newAnimaPosition, new Animal(newAnimaPosition));
//
//            if (this.possibleJunglePositions.contains(newAnimaPosition)) {
//
//                this.possibleJunglePositions.remove(newAnimaPosition);
//            }else{
//                this.possibleStepPositions.remove(newAnimaPosition);
//            }
        }
    }

    //TODO to jest raczej zbedne
    public List<Animal> getAnimalsOnMap(){
        List<Animal> animalsOnMapList = new ArrayList<>();

        for(List<Animal> animals : this.animalsOnMap.values()){
            animalsOnMapList.addAll(animals);
        }

        return animalsOnMapList;
    }


    public void findJungleCorners(){
        int a = (int) Math.floor(Math.sqrt(jungleRatio)*this.width);
        int b = (int) Math.floor(Math.sqrt(jungleRatio)*this.height);

        this.jungleLowerLeft = new Vector2d((width - a)/2,(height - b)/2);
        this.jungleUpperRight = new Vector2d(this.jungleLowerLeft.x + a,b + this.jungleLowerLeft.y);

    }

    public void addPositions(int i, int j){
        if(i >= jungleLowerLeft.x && i <= jungleUpperRight.x && j >= jungleLowerLeft.y && j <= jungleUpperRight.y){
            this.possibleJunglePositions.add(new Vector2d(i,j));
        }
        else{
            this.possibleStepPositions.add(new Vector2d(i,j));
        }
//        this.allPossiblePositions.add(new Vector2d(i,j)); //TODO to mozna chyba zrobic mergowaniem
    }

    public boolean checkPointInJungle(int i, int j){
        return i >= jungleLowerLeft.x && i <= jungleUpperRight.x && j >= jungleLowerLeft.y && j <= jungleUpperRight.y;
    }

    public void findStepAndJunglePositions(){
        for(int i = 0; i <= width; i++){
            for(int j = 0; j <= height; j++){
                addPositions(i,j);
            }
        }
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        AbstractWorldMapElement object = objectAt(position);
        if(object instanceof Grass){
            this.elementsOnMap.remove(position);
        }
        return true;
    }

    @Override
    public boolean place(Animal animal) {
        if(canMoveTo(animal.getPosition())){

            if(this.elementsOnMap.containsKey(animal.getPosition())){
                this.elementsOnMap.get(animal.getPosition()).add(animal);
                this.animalsOnMap.get(animal.getPosition()).add(animal);
            }else{
                this.elementsOnMap.put(animal.getPosition(), new ArrayList<>(List.of(animal)));
                this.animalsOnMap.put(animal.getPosition(), new ArrayList<>(List.of(animal)));
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
                if(this.elementsOnMap.containsKey(oldPosition) && this.elementsOnMap.get(oldPosition).size() == 1){
                    this.elementsOnMap.remove(oldPosition);

                }else{
                    this.elementsOnMap.get(oldPosition).remove(animal);
                }

                //TODO
                //tutaj jest gdzie bląd

                if(this.animalsOnMap.containsKey(oldPosition) && this.animalsOnMap.get(oldPosition).size() == 1){
                    this.animalsOnMap.remove(oldPosition);
                }else{
                    System.out.println(this.animalsOnMap);
                    this.animalsOnMap.get(oldPosition).remove(animal);
                }

                if(this.elementsOnMap.containsKey(newPosition)) { //here np
                    this.elementsOnMap.get(newPosition).add(animal);

                }
                else {
                    this.elementsOnMap.put(newPosition, new ArrayList<>(List.of(animal)));

                }
                if( this.animalsOnMap.containsKey(newPosition)){
                    this.animalsOnMap.get(newPosition).add(animal);
                }
                else{
                    this.animalsOnMap.put(newPosition, new ArrayList<>(List.of(animal)));
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
                this.elementsOnMap.get(jungleGrassPosition).add(new Grass(jungleGrassPosition,this.plantEnergy));
            }else{
                this.elementsOnMap.put(jungleGrassPosition, Collections.singletonList(new Grass(jungleGrassPosition,this.plantEnergy)));

            }

            this.grassOnMap.put(jungleGrassPosition, new Grass(jungleGrassPosition,this.plantEnergy));
            this.possibleJunglePositions.remove(jungleGrassPosition);
        }

        if(this.possibleStepPositions.size() > 0){
            Vector2d stepGrassPosition = getRandom(this.possibleStepPositions);

            if(this.elementsOnMap.containsKey(stepGrassPosition)){
                this.elementsOnMap.get(stepGrassPosition).add(new Grass(stepGrassPosition,this.plantEnergy));
            }else{
                this.elementsOnMap.put(stepGrassPosition, Collections.singletonList(new Grass(stepGrassPosition,this.plantEnergy)));

            }
            this.grassOnMap.put(stepGrassPosition, new Grass(stepGrassPosition,this.plantEnergy));
            this.possibleStepPositions.remove(stepGrassPosition);
        }
    }

    public Animal getAnimalOnMap(Vector2d vector2d){
        if(this.animalsOnMap.containsKey(vector2d)){
            return this.animalsOnMap.get(vector2d).get(0);
        }
        else{
            return null;
        }
    }

    public Grass getGrassOnMap(Vector2d vector2d){
        return this.grassOnMap.getOrDefault(vector2d, null);
    }
}
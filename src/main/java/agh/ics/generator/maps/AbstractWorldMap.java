package agh.ics.generator.maps;

import agh.ics.generator.mapelements.Grass;
import agh.ics.generator.mapelements.Vector2d;
import agh.ics.generator.mapelements.AbstractWorldMapElement;
import agh.ics.generator.mapelements.animal.Animal;
import agh.ics.generator.interfaces.IPositionChangeObserver;
import agh.ics.generator.interfaces.IWorldMap;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected final HashMap<Vector2d, List<AbstractWorldMapElement>> elementsOnMap = new HashMap<>();
    protected final HashMap<Vector2d, List<Animal>> animalsOnMap = new HashMap<>();
    protected final HashMap<Vector2d, Grass> grassOnMap = new HashMap<>();
    protected int startEnergy;
    protected int width;
    protected int height;
    protected double jungleRatio;
    protected int moveEnergy;
    protected int plantEnergy;
    ;
    protected HashSet<Vector2d> possibleStepPositions = new HashSet<>();
    protected HashSet<Vector2d> possibleJunglePositions = new HashSet<>();
    Vector2d jungleLowerLeft;
    Vector2d jungleUpperRight;

    public AbstractWorldMap(double jungleRatio, int width, int height, int numberOfStartingAnimals, int startEnergy,
                            int moveEnergy, int plantEnergy) {
        this.jungleRatio = jungleRatio;
        this.width = width-1;
        this.height = height-1;
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

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth(){
        return this.width;
    }

    public int getMoveEnergy(){
        return this.moveEnergy;
    }
    public int getHeight(){
        return this.height;
    }

    public HashMap<Vector2d, List<AbstractWorldMapElement>> getElementsOnMap(){
        return this.elementsOnMap;
    }

    public Vector2d getJungleLowerLeft(){
        return this.jungleLowerLeft;
    }

    public Vector2d getJungleUpperRight(){
        return this.jungleUpperRight;
    }

    public HashSet<Vector2d> getPossibleStepPositions(){
        return this.possibleStepPositions;
    }

    public HashSet<Vector2d> getPossibleJunglePositions(){
        return this.possibleJunglePositions;
    }

    public int getStartEnergy(){
        return this.startEnergy;
    }

    public HashMap<Vector2d, Grass> getGrassOnMap(){
        return this.grassOnMap;
    }

    public HashMap<Vector2d, List<Animal>> getAnimalsOnMap(){
        return this.animalsOnMap;
    }

    public void generateStartAnimals(int numberOfStartingAnimals) {
        for (int i = 0; i < numberOfStartingAnimals; i++) {
            if (this.possibleJunglePositions.size() < 1) {
                return;
            }
            Vector2d newAnimaPosition = getRandom(this.possibleJunglePositions);
            this.place(new Animal(this, newAnimaPosition, this.startEnergy));
        }
    }

    public List<Animal> getAnimalsOnMapList() {
        List<Animal> animalsOnMapList = new ArrayList<>();

        for (List<Animal> animals : this.animalsOnMap.values()) {
            animalsOnMapList.addAll(animals);
        }

        return animalsOnMapList;
    }

    public void findJungleCorners() {
        int a = (int) Math.floor(Math.sqrt(jungleRatio) * this.width);
        int b = (int) Math.floor(Math.sqrt(jungleRatio) * this.height);

        this.jungleLowerLeft = new Vector2d((width - a) / 2, (height - b) / 2);
        this.jungleUpperRight = new Vector2d(this.jungleLowerLeft.x + a, b + this.jungleLowerLeft.y);

    }

    public void addPositions(int i, int j) {
        if (i >= jungleLowerLeft.x && i <= jungleUpperRight.x && j >= jungleLowerLeft.y && j <= jungleUpperRight.y) {
            this.possibleJunglePositions.add(new Vector2d(i, j));
        } else {
            this.possibleStepPositions.add(new Vector2d(i, j));
        }
    }

    public boolean checkPointInJungle(int i, int j) {
        return i >= jungleLowerLeft.x && i <= jungleUpperRight.x && j >= jungleLowerLeft.y && j <= jungleUpperRight.y;
    }

    public void findStepAndJunglePositions() {
        for (int i = 0; i <= width; i++) {
            for (int j = 0; j <= height; j++) {
                addPositions(i, j);
            }
        }
    }

    public void removeGrass(Vector2d position) {
        AbstractWorldMapElement object = objectAt(position);
        if (object instanceof Grass) {
            this.elementsOnMap.remove(position);
        }
    }

    @Override
    public void place(Animal animal) {
        removeGrass(animal.getPosition());
        if (this.elementsOnMap.containsKey(animal.getPosition())) {
            if(this.elementsOnMap.get(animal.getPosition()) == null || this.animalsOnMap.get(animal.getPosition()) == null){
                System.out.println("ERRROR");
            }
            this.elementsOnMap.get(animal.getPosition()).add(animal);
            this.animalsOnMap.get(animal.getPosition()).add(animal);
        } else {
            this.elementsOnMap.put(animal.getPosition(), new ArrayList<>(List.of(animal)));
            this.animalsOnMap.put(animal.getPosition(), new ArrayList<>(List.of(animal)));
        }

        if (checkPointInJungle(animal.getPosition().x, animal.getPosition().y)) {
            this.possibleJunglePositions.remove(animal.getPosition());
        } else {
            this.possibleStepPositions.remove(animal.getPosition());
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public AbstractWorldMapElement objectAt(Vector2d position) {
        if (this.elementsOnMap.get(position) != null) {
            return this.elementsOnMap.get(position).get(0);
        } else {
            return null;
        }
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        AbstractWorldMapElement object = objectAt(oldPosition);
        removeGrass(newPosition);
//        System.out.println(elementsOnMap);
        if (object instanceof Animal) {
            updateElementsOnMap(newPosition,oldPosition,animal);
            updateAnimalsOnMap(newPosition,oldPosition,animal);
        }
//        System.out.println(elementsOnMap);
    }

    public void updateElementsOnMap(Vector2d newPosition, Vector2d oldPosition, Animal animal){
        if(this.elementsOnMap.containsKey(oldPosition)){
            this.elementsOnMap.get(oldPosition).remove(animal);
            if (this.elementsOnMap.get(oldPosition).size() == 0){
                this.elementsOnMap.remove(oldPosition);
                this.addPositions(oldPosition.x,oldPosition.y);
            }
        }
        if(this.elementsOnMap.containsKey(newPosition)) {
            this.elementsOnMap.get(newPosition).add(animal);
        }
        else {
            this.elementsOnMap.put(newPosition, new ArrayList<>(List.of(animal)));
        }
    }

    public void updateAnimalsOnMap(Vector2d newPosition, Vector2d oldPosition, Animal animal){
        if(this.animalsOnMap.containsKey(oldPosition)){
            this.animalsOnMap.get(oldPosition).remove(animal);
            if (this.animalsOnMap.get(oldPosition).size() == 0){
                this.animalsOnMap.remove(oldPosition);
                this.addPositions(oldPosition.x,oldPosition.y);
            }
        }
        if( this.animalsOnMap.containsKey(newPosition)){
            this.animalsOnMap.get(newPosition).add(animal);
        }
        else{
            this.animalsOnMap.put(newPosition, new ArrayList<>(List.of(animal)));
        }
    }

    public Vector2d getRandom(HashSet<Vector2d> hashSet) {
        Vector2d[] arrayNumbers = hashSet.toArray(new Vector2d[0]);
        int rnd = new Random().nextInt(hashSet.size());
        return arrayNumbers[rnd];
    }

    public void generateGrass() {
        if (this.possibleJunglePositions.size() > 0) {
            Vector2d jungleGrassPosition = getRandom(this.possibleJunglePositions);
            addGrassOnMap(this.elementsOnMap,jungleGrassPosition);

            this.grassOnMap.put(jungleGrassPosition, new Grass(jungleGrassPosition, this.plantEnergy));
            this.possibleJunglePositions.remove(jungleGrassPosition);
        }

        if (this.possibleStepPositions.size() > 0) {
            Vector2d stepGrassPosition = getRandom(this.possibleStepPositions);
            addGrassOnMap(this.elementsOnMap,stepGrassPosition);

            this.grassOnMap.put(stepGrassPosition, new Grass(stepGrassPosition, this.plantEnergy));
            this.possibleStepPositions.remove(stepGrassPosition);
        }
    }

    public void addGrassOnMap(HashMap<Vector2d,List<AbstractWorldMapElement>> set,Vector2d position){
        if (set.containsKey(position)) {
            set.get(position).add(new Grass(position, this.plantEnergy));
        } else {
            set.put(position, Collections.singletonList(new Grass(position, this.plantEnergy)));
        }
    }

    public Animal getAnimalOnMap(Vector2d vector2d) {
        if (this.animalsOnMap.containsKey(vector2d)) {
            return this.animalsOnMap.get(vector2d).get(0);
        } else {
            return null;
        }
    }

    public void removeAnimalFromMap(Vector2d position, Animal animal){
        if(this.elementsOnMap.containsKey(position)){
            this.elementsOnMap.get(position).remove(animal);
            if (this.elementsOnMap.get(position).size() == 0){
                this.elementsOnMap.remove(position);
                this.addPositions(position.x,position.y);
            }
        }
    }

    public Grass getGrassOnMap(Vector2d vector2d) {
        return this.grassOnMap.getOrDefault(vector2d, null);
    }
}
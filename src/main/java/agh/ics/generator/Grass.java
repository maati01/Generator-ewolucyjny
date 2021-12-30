package agh.ics.generator;

public class Grass extends AbstractWorldMapElement {
    private final Vector2d position;
    private final int plantEnergy;

    public Grass(Vector2d position,int plantEnergy){
        this.position = position;
        this.plantEnergy = plantEnergy;
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getPlantEnergy(){
        return this.plantEnergy;
    }
    @Override
    public String getImagePath() {
        return "src/main/resources/grass.png";
    }

    public String toString(){
        return "*";
    }

    public String toStringInGui(){
        return null;
    }
}

package agh.ics.generator;

public class Grass extends AbstractWorldMapElement {
    private final Vector2d position;

    public Grass(Vector2d position){
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String getImagePath() {
        return "src/main/resources/grass.png";
    }

    public String toString(){
        return "*";
    }

    public String toStringInGui(){
        return "Grass";
    }
}

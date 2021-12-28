package agh.ics.generator;

public abstract class AbstractWorldMapElement implements IMapElement {
    protected Vector2d position = new Vector2d(0,0);
    protected int energy = 2;

    public Vector2d getPosition() {
        return this.position;
    }

    public int getEnergy(){return this.energy;}
}


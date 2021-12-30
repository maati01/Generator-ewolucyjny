package agh.ics.generator;

import agh.ics.generator.interfaces.IMapElement;

public abstract class AbstractWorldMapElement implements IMapElement {
    protected Vector2d position;

    public Vector2d getPosition() {
        return this.position;
    }

}


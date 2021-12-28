package agh.ics.generator;

import agh.ics.generator.interfaces.IWorldMap;

public class BoundedGrassField extends AbstractWorldMap implements IWorldMap {
    public BoundedGrassField(double jungleRatio){
        super(jungleRatio);
    }
}

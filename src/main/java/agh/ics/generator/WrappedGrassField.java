package agh.ics.generator;

import agh.ics.generator.interfaces.IWorldMap;

public class WrappedGrassField extends AbstractWorldMap implements IWorldMap {
    public WrappedGrassField(double jungleRatio,int width, int height, int numberOfStartingAnimals,int startEnergy,int moveEnergy, int planEnergy) {
        super(jungleRatio,width,height,numberOfStartingAnimals,startEnergy,moveEnergy, planEnergy);
    }
}

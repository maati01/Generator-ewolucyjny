package agh.ics.generator.maps;

import agh.ics.generator.interfaces.IWorldMap;

public class BoundedGrassField extends AbstractWorldMap implements IWorldMap {
    public BoundedGrassField(double jungleRatio,int width, int height,int numberOfStartingAnimals,int startEnergy,int moveEnergy,int plantEnergy) {
        super(jungleRatio,width,height,numberOfStartingAnimals,startEnergy, moveEnergy, plantEnergy);
    }
}

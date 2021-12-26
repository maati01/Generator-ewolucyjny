package agh.ics.generator;

public class BoundedGrassField extends AbstractWorldMap implements IWorldMap{
    public BoundedGrassField(int numberGrasses){
        this.numberGrasses = numberGrasses;
        generateGrass(this.numberGrasses);

    }
}

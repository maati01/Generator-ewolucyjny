package agh.ics.generator;

public class WrappedGrassField extends AbstractWorldMap implements IWorldMap{
    public WrappedGrassField(int numberGrasses){
        this.numberGrasses = numberGrasses;
        generateGrass(this.numberGrasses);

    }

//    @Override
//    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
//        AbstractWorldMapElement object = objectAt(oldPosition);
//        if (object instanceof Animal) {
//            this.elementsOnMap.put(newPosition, object);
//            this.elementsOnMap.remove(oldPosition, object);
//        }
//    }
}

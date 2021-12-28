package agh.ics.generator;

import agh.ics.generator.interfaces.IWorldMap;

public class WrappedGrassField extends AbstractWorldMap implements IWorldMap {
    public WrappedGrassField(double jungleRatio) {
        super(jungleRatio);
    }
//    int rangeX = upperRight.x - lowerLeft.x;
//    int rangeY = upperRight.y - lowerLeft.y;
//    Vector2d jungleLowerLeft;
//    Vector2d jungleUpperRight;

//    public WrappedGrassField(int numberGrasses,double jungleRatio){
//        this.numberGrasses = numberGrasses;
//        generateGrass(this.numberGrasses);
//        this.jungleRatio = jungleRatio;
//        this.jungleLowerLeft = new Vector2d((int)Math.ceil(upperRight.x*jungleRatio),
//                (int)Math.ceil(upperRight.y*jungleRatio));
//        this.jungleUpperRight = new Vector2d(upperRight.x - this.jungleLowerLeft.x,
//                upperRight.y - this.jungleLowerLeft.y);
//    }

//    public void findPositions(double jungleRatio){
//        for(int i = 0; i <= rangeX; i++){
//            for(int j = 0; j <= rangeY; j++){
//                if()
//            }
//        }
//    }
//    @Override
//    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
//        AbstractWorldMapElement object = objectAt(oldPosition);
//        if (object instanceof Animal) {
//            this.elementsOnMap.put(newPosition, object);
//            this.elementsOnMap.remove(oldPosition, object);
//        }
//    }
}

package agh.ics.generator;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition);
    /**
     * Method removes the element from the map
     * and then puts it in a new position
     * @param oldPosition
     * @param newPosition
     */


}

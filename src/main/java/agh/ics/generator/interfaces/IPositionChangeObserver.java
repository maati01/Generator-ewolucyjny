package agh.ics.generator.interfaces;

import agh.ics.generator.Animal;
import agh.ics.generator.Vector2d;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal);
    /**
     * Method removes the element from the map
     * and then puts it in a new position
     * @param oldPosition
     * @param newPosition
     */


}

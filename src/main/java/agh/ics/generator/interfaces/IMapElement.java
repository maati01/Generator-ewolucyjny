package agh.ics.generator.interfaces;

import agh.ics.generator.Vector2d;

public interface IMapElement {

    Vector2d getPosition();
    /*
     Returns position of element on map
    */

    String getImagePath();
    /*
     Returns the path to the image.
     */

    String toStringInGui();
    /*
     Returns the string below the image
     */
}

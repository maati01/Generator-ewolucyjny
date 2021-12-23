package agh.ics.generator;

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

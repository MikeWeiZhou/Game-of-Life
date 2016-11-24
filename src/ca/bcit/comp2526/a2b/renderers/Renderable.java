package ca.bcit.comp2526.a2b.renderers;

import java.awt.Color;
import java.awt.Point;

/**
 * Renderable.
 *
 * @author  Wei Zhou
 * @version 2016-11-20
 * @since   2016-11-20
 */
public interface Renderable {

    /**
     * Returns the Color of Renderable object.
     * @return Color
     */
    Color getColor();

    /**
     * Returns the size of Renderable object.
     * @return length
     */
    int getSize();

    /**
     * Returns the Point of Renderable object.
     * @return Point
     */
    Point getPoint();
}

package ca.bcit.comp2526.a2a.lifeforms;

import ca.bcit.comp2526.a2a.grids.Node;

import java.awt.*;

/**
 * Lifeless.
 *
 * @author  Wei Zhou
 * @version 2016-11-08
 * @since   2016-11-08
 */
public class Lifeless extends Lifeform {

    private static final Color DEFAULT_COLOR = Color.white;

    /**
     * Constructs a Lifeless.
     * @param node    that contains this Lifeless
     */
    public Lifeless(final Node node) {
        super(node);
        setLifeformType(LifeformType.LIFELESS);
        setColor(DEFAULT_COLOR);
    }

    /**
     * Initializes this Lifeless.
     */
     public void init() {}
}

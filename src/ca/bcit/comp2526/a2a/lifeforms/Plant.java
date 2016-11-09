package ca.bcit.comp2526.a2a.lifeforms;

import ca.bcit.comp2526.a2a.grids.Node;

import java.awt.Color;

/**
 * Plant.
 *
 * @author  Wei Zhou
 * @version 2016-11-08
 * @since   2016-11-07
 */
public class Plant extends Lifeform {

    private static final Color DEFAULT_COLOR = Color.green;

    /**
     * Constructs a Plant.
     * @param node    that contains this Plant
     */
    public Plant(final Node node) {
        super(node);
        setLifeformType(LifeformType.PLANT);
        setColor(DEFAULT_COLOR);
    }

    /**
     * Initializes this Plant.
     */
    public void init() {}
}

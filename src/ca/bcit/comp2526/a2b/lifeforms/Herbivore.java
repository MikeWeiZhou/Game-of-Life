package ca.bcit.comp2526.a2b.lifeforms;

import ca.bcit.comp2526.a2b.grids.Node;

import java.awt.*;

/**
 * Herbivore.
 *
 * @author  Wei Zhou
 * @version 2016-11-08
 * @since   2016-11-08
 */
public class Herbivore extends Animal {

    private static final Color DEFAULT_COLOR = Color.black;

    /**
     * Constrcuts a Herbivore.
     * @param node    that contains this Herbivore
     */
    public Herbivore(final Node node) {
        super(node);
        setLifeformType(LifeformType.HERBIVORE);
        setColor(DEFAULT_COLOR);
    }

    /**
     * Initializes this Herbivore.
     */
    public void init() {}
}

package ca.bcit.comp2526.a2b.lifeforms;

import ca.bcit.comp2526.a2b.grids.Node;

/**
 * Animal.
 *
 * @author Wei Zhou
 * @version 2016-11-08
 * @since 2016-11-08
 */
public abstract class Animal extends Lifeform {

    /**
     * Constructs an Animal.
     * @param node    that contains this Animal
     */
    public Animal(final Node node) {
        super(node);
    }
}

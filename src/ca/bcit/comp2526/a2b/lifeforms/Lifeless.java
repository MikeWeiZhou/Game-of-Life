package ca.bcit.comp2526.a2b.lifeforms;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Node;

/**
 * Lifeless; does nothing.
 *
 * @author  Wei Zhou
 * @version 2016-11-14
 * @since   2016-11-14
 */
public class Lifeless extends Lifeform {

    private static final LifeformType TYPE = LifeformType.LIFELESS;

    /**
     * Constructs a new Lifeless that does nothing.
     * @param node    that contains this Lifeless
     * @param world   that contains this Lifeless
     */
    public Lifeless(final Node node, final World world) {
        super(TYPE, node, world);
    }

    /**
     * Override initiation and kill Lifeless immediately.
     */
    @Override
    public void init() {
        kill();
    }
}

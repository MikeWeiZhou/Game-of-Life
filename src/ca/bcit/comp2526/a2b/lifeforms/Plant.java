package ca.bcit.comp2526.a2b.lifeforms;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Node;
import ca.bcit.comp2526.a2b.grids.Terrain;

import java.awt.Color;

/**
 * Plant.
 *
 * @author  Wei Zhou
 * @version 2016-11-13
 * @since   2016-11-07
 */
public class Plant extends Lifeform {

    private static final LifeformType TYPE;
    private static final Color        COLOR;
    private static final int          HEALTH;
    private static final int          VISION;
    private static final int          R_NEIGHBORS;
    private static final int          R_EMPTY;
    private static final int          R_MAX_BABIES;
    private static final Terrain      INHABITABLE;

    static {
        TYPE        = LifeformType.PLANT;
        COLOR       = Color.green;
        HEALTH      = 10;
        VISION      = 1;
        INHABITABLE = Terrain.WATER;

        R_NEIGHBORS  = 3;
        R_EMPTY      = 2;
        R_MAX_BABIES = 2;
    }

    /**
     * Constructs a new Plant.
     * @param node    that contains this Plant
     * @param world   that contains this Plant
     */
    public Plant(final Node node, final World world) {
        super(TYPE, node, world);
        setDefaultColor(COLOR);
        setMaxHealth(HEALTH);
        setVisionLevel(VISION);
        setSexConditions(R_NEIGHBORS, R_EMPTY, R_MAX_BABIES);
        setInhabitable(INHABITABLE);

        addTrait(Trait.HERBIVORE_EDIBLE);
        addTrait(Trait.OMNIVORE_EDIBLE);
    }
}

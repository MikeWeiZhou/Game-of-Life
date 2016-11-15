package ca.bcit.comp2526.a2b.lifeforms;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Node;

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
    private static final float        DEATH_RATIO;
    private static final int          R_NEIGHBORS;
    private static final int          R_EMPTY;
    private static final int          R_MAX_BABIES;

    static {
        TYPE        = LifeformType.PLANT;
        COLOR       = Color.green;
        HEALTH      = 10;
        VISION      = 1;
        DEATH_RATIO = 0;

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
        setNaturalDeathRatio(DEATH_RATIO);

        addTrait(Trait.CARNIVORE_EDIBLE);
        addTrait(Trait.HERBIVORE_EDIBLE);
        addTrait(Trait.OMNIVORE_EDIBLE);
    }
}

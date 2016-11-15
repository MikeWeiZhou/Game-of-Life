package ca.bcit.comp2526.a2b.lifeforms;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Node;

import java.awt.Color;

/**
 * Omnivore.
 *
 * @author  Wei Zhou
 * @version 2016-11-13
 * @since   2016-11-13
 */
public class Omnivore extends Animal {

    private static final LifeformType TYPE;
    private static final Color        COLOR;
    private static final int          HEALTH;
    private static final int          VISION;
    private static final Trait        TARGET;
    private static final float        DEATH_RATIO;

    private static final int          R_NEIGHBORS;
    private static final int          R_EMPTY;
    private static final int          R_MAX_BABIES;
    private static final int          R_FOOD;

    static {
        TYPE        = LifeformType.OMNIVORE;
        COLOR       = Color.magenta;
        HEALTH      = 4;
        VISION      = 1;
        TARGET      = Trait.OMNIVORE_EDIBLE;
        DEATH_RATIO = 0.1f;

        R_NEIGHBORS  = 1;
        R_EMPTY      = 3;
        R_MAX_BABIES = 1;
        R_FOOD       = 3;
    }

    /**
     * Constructs a new Omnivore.
     * @param node   that contains this Omnivore
     * @param world  that contains this Omnivore
     */
    public Omnivore(final Node node, final World world) {
        super(TYPE, node, world);
        setDefaultColor(COLOR);
        setMaxHealth(HEALTH);
        setVisionLevel(VISION);
        setSexConditions(R_NEIGHBORS, R_EMPTY, R_MAX_BABIES, R_FOOD);
        setTargetTrait(TARGET);
        setNaturalDeathRatio(DEATH_RATIO);

        addTrait(Trait.CARNIVORE_EDIBLE);
        addTrait(Trait.OMNIVORE_EDIBLE);
    }
}

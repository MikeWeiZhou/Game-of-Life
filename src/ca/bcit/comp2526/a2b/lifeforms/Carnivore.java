package ca.bcit.comp2526.a2b.lifeforms;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Node;
import ca.bcit.comp2526.a2b.grids.Terrain;

import java.awt.Color;

/**
 * Carnivore.
 *
 * @author  Wei Zhou
 * @version 2016-11-14
 * @since   2016-11-13
 */
public class Carnivore extends Animal {

    private static final LifeformType TYPE;
    private static final Color        COLOR;
    private static final int          HEALTH;
    private static final int          MOVEMENT;
    private static final Trait        TARGET;
    private static final Terrain      INHABITABLE;

    private static final int          R_PARTNERS;
    private static final int          R_EMPTY;
    private static final int          R_MAX_BABIES;
    private static final int          R_FOOD;

    static {
        TYPE        = LifeformType.CARNIVORE;
        COLOR       = Color.red;
        HEALTH      = 3;
        MOVEMENT    = 2;
        TARGET      = Trait.CARNIVORE_EDIBLE;
        INHABITABLE = Terrain.WATER;

        R_PARTNERS   = 1;
        R_EMPTY      = 3;
        R_MAX_BABIES = 1;
        R_FOOD       = 3;
    }

    /**
     * Constructs a new Carnivore.
     * @param node   that contains this Carnivore
     * @param world  that contains this Carnivore
     */
    public Carnivore(final Node node, final World world) {
        super(TYPE, node, world);
        setColor(COLOR);
        setDefaultColor(COLOR);
        setHealth(HEALTH);
        setMaxHealth(HEALTH);
        setMovement(MOVEMENT);
        setSexConditions(R_PARTNERS, R_EMPTY, R_MAX_BABIES, R_FOOD);
        setTargetTrait(TARGET);
        setInhabitable(INHABITABLE);

        addTrait(Trait.OMNIVORE_EDIBLE);
    }
}

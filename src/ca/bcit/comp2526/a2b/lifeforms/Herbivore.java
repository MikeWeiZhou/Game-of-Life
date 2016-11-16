package ca.bcit.comp2526.a2b.lifeforms;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Node;

import java.awt.Color;

/**
 * Herbivore.
 *
 * @author  Wei Zhou
 * @version 2016-11-13
 * @since   2016-11-08
 */
public class Herbivore extends Animal {

    private static final LifeformType TYPE;
    private static final Color        COLOR;
    private static final int          HEALTH;
    private static final int          MOVEMENT;
    private static final Trait        TARGET;

    private static final int          R_PARTNERS;
    private static final int          R_EMPTY;
    private static final int          R_MAX_BABIES;
    private static final int          R_FOOD;

    static {
        TYPE     = LifeformType.HERBIVORE;
        COLOR    = Color.yellow;
        HEALTH   = 6;
        MOVEMENT = 1;
        TARGET   = Trait.HERBIVORE_EDIBLE;

        R_PARTNERS   = 1;
        R_EMPTY      = 2;
        R_MAX_BABIES = 2;
        R_FOOD       = 2;
    }

    /**
     * Constructs a new Herbivore.
     * @param node    that contains this Herbivore
     * @param world  that contains this Herbivore
     */
    public Herbivore(final Node node, final World world) {
        super(TYPE, node, world);
        setColor(COLOR);
        setDefaultColor(COLOR);
        setHealth(HEALTH);
        setMaxHealth(HEALTH);
        setMovement(MOVEMENT);
        setSexConditions(R_PARTNERS, R_EMPTY, R_MAX_BABIES, R_FOOD);
        setTargetTrait(TARGET);

        addTrait(Trait.CARNIVORE_EDIBLE);
        addTrait(Trait.OMNIVORE_EDIBLE);
    }
}

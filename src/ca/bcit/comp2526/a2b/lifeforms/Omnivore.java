package ca.bcit.comp2526.a2b.lifeforms;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Node;

import java.awt.Color;

/**
 * Omnivore.
 *
 * <p>
 *     A well balanced World with only Omnivores and Plants. Omnivores with
 *     5.5% mortality paired with Plants that have a 1% death rate seem to
 *     be a match made in heaven.
 * </p>
 *
 * @author  Wei Zhou
 * @version 2016-11-13
 * @since   2016-11-13
 */
public class Omnivore extends Animal {

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
        TYPE     = LifeformType.OMNIVORE;
        COLOR    = Color.magenta;
        HEALTH   = 4;
        MOVEMENT = 1;
        TARGET   = Trait.OMNIVORE_EDIBLE;

        R_PARTNERS   = 1;
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
        setColor(COLOR);
        setDefaultColor(COLOR);
        setHealth(HEALTH);
        setMaxHealth(HEALTH);
        setMovement(MOVEMENT);
        setSexConditions(R_PARTNERS, R_EMPTY, R_MAX_BABIES, R_FOOD);
        setTargetTrait(TARGET);

        addTrait(Trait.CARNIVORE_EDIBLE);
    }
}

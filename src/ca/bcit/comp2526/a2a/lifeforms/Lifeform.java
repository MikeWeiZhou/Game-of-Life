package ca.bcit.comp2526.a2a.lifeforms;

import ca.bcit.comp2526.a2a.traits.Trait;
import ca.bcit.comp2526.a2a.traits.TraitType;
import ca.bcit.comp2526.a2a.actions.Action;
import ca.bcit.comp2526.a2a.actions.ActionType;
import ca.bcit.comp2526.a2a.grids.Node;

import java.awt.Color;
import java.util.*;

/**
 * Lifeform.
 *
 * @author  Wei Zhou
 * @version 2016-11-08
 * @since   2016-11-06
 */
public abstract class Lifeform {

    private final Set<ActionType>       actions;
    private final Map<TraitType, Trait> traits;
    private       Node                  node;
    private       LifeformType          type;
    private       Color                 color;
    private       boolean               alive;

    /**
     * Constructs a new Lifeform.
     * @param node    that contains this Lifeform
     */
    public Lifeform(final Node node) {
        this.actions = new TreeSet<ActionType>();
        this.traits  = new HashMap<TraitType, Trait>();
        this.node    = node;
        this.alive   = true;
    }

    /**
     * Initializes this Lifeform.
     */
    public abstract void init();

    /**
     * Kills this Lifeform.
     */
    public void kill() {
        alive = false;
    }

    /**
     * Take actions.
     */
    public void takeActions() {
        final Iterator<ActionType> action = actions.iterator();
        while(action.hasNext()) {
            Action.takeAction(action.next(), this);
        }
    }

    /**
     * Adds an Action to this Lifeform.
     * @param at    ActionType
     */
    public void addAction(final ActionType at) {
        actions.add(at);
    }

    /**
     * Returns the specified Trait.
     * @param tt    TraitType
     * @return Trait
     */
    public Trait getTrait(final TraitType tt) {
        return traits.get(tt);
    }

    /**
     * Adds a Trait to this Lifeform.
     * @param trait    to add
     */
    public void addTrait(final TraitType tt, final Trait trait) {
        traits.put(tt, trait);
    }

    /**
     * Returns the Node that this Lifeform is in.
     * @return Node
     */
    public Node getNode() {
        return node;
    }

    /**
     * Sets LifeformType for this Lifeform.
     * @param type    of Lifeform
     */
    public void setLifeformType(final LifeformType type) {
        this.type = type;
    }

    /**
     * Sets the Color of this Lifeform.
     * @param color of Lifeform
     */
    public void setColor(final Color color) {
        this.color = color;
    }

    /**
     * Returns the Color of this Lifeform.
     * @return Color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns true if this Lifeform is alive.
     * @return boolean
     */
    public boolean isAlive() {
        return alive;
    }
}

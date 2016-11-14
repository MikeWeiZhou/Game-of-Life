package ca.bcit.comp2526.a2b.lifeforms;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Node;
import ca.bcit.comp2526.a2b.grids.Terrain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * Lifeform.
 *
 * @author  Wei Zhou
 * @version 2016-11-13
 * @since   2016-11-06
 */
public abstract class Lifeform {

    private final int MAX_COLOR_INTENSITY = 255;

    private final World        world;
    private final Set<Trait>   traits;
    private final LifeformType type;
    private       Node         node;
    private       boolean      alive;
    private       Color        color;
    private       Color        defaultColor;
    private       int          health;
    private       int          maxHealth;
    private       int          visionLevel;
    private       Trait        targetTrait;
    private       Terrain      inhabitable;

    private       int          repNeighborsAlike;
    private       int          repNeighborsEmpty;
    private       int          repNeighborsFood;
    private       int          repMaxBabies;

    /**
     * Constructs a new Lifeform.
     * @param lft     LifeformType
     * @param node    that contains this Lifeform
     * @param world   that contains this Lifeform
     */
    public Lifeform(final LifeformType lft, final Node node, final World world) {
        this.traits = new HashSet<Trait>();
        this.type   = lft;
        this.node   = node;
        this.world  = world;
        this.alive  = true;
    }

    /**
     * Throws an error if Lifeform is in an illegal state.
     */
    public void init() {
        if (color == null || health == 0 || repNeighborsAlike == 0 ||
                  repNeighborsEmpty == 0 || repMaxBabies == 0) {
            throw new IllegalStateException();
        }
    }

    /**
     * Reproduce if sex conditions met.
     */
    public Lifeform[] reproduce() {
        final List<Node> emptyNodes;
        final Node[]     nearby;

        int partnersNearby = 0;
        int emptyNearby    = 0;
        int foodNearby     = 0;
            emptyNodes     = new ArrayList<Node>();
            nearby         = getNode().getNeighborsForLevel(getVisionLevel());

        // determine conditions
        for (Node n : nearby) {
            Lifeform lf = getWorld().getLifeformAt(n);
            if (lf == null) {
                emptyNearby++;
                emptyNodes.add(n);
            } else {
                if (lf.getLifeformType().equals(getLifeformType())) {
                    partnersNearby++;
                } else if (lf.hasTrait(getTargetTrait())) {
                    foodNearby++;
                }
            }
        }

        if (partnersNearby >= repNeighborsAlike && emptyNearby >= repNeighborsEmpty
                && foodNearby >= repNeighborsFood) {
            return makeBabies(emptyNodes.iterator(), repMaxBabies);
        }

        // return an array of size 0; no newborns
        return new Lifeform[0];
    }

    /**
     * Take action.
     */
    public void takeAction() {
        if (!getNode().getTerrain().equals(Terrain.WATER)) {
            setHealth(getHealth() - 1);
        }

        if (health <= 0) {
            kill();
        } else {
            darkenColor();
        }
    }

    /**
     * Returns the World that this Lifeform is in.
     * @return World
     */
    public World getWorld() {
        return world;
    }

    /**
     * Returns inhabitable Terrain for this Animal.
     */
    protected Terrain getInhabitable() {
        return inhabitable;
    }

    /**
     * Returns the Node that this Lifeform is in.
     * @return Node
     */
    public Node getNode() {
        return node;
    }

    /**
     * Returns the max health for this Lifeform.
     * @return max health
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Returns true if this Lifeform has specified Trait.
     * @param trait    to check
     * @return true if Trait exists
     */
    public boolean hasTrait(final Trait trait) {
        return traits.contains(trait);
    }

    /**
     * Returns LifeformType for this Lifeform.
     * @return LifeformType
     */
    public LifeformType getLifeformType() {
        return type;
    }

    /**
     * Returns the targeted Trait of this Lifeform.
     */
    protected Trait getTargetTrait() {
        return targetTrait;
    }

    /**
     * Returns the Color of this Lifeform.
     * @return Color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the default Color of this Lifeform.
     * @return default Color
     */
    public Color getDefaultColor() {
        return defaultColor;
    }

    /**
     * Returns the vision level for this animal.
     */
    protected int getVisionLevel() {
        return visionLevel;
    }

    /**
     * Returns the health of this Lifeform.
     * @return health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Returns true if this Lifeform is alive.
     * @return boolean
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Sets the reproduction requirements of this Lifeform.
     * @param neighbors    minimum number of same species nearby
     * @param empty        minimum empty Nodes nearby
     * @param maxBabies    that can be reproduced
     */
    protected void setSexConditions(final int neighbors, final int empty,
                                                         final int maxBabies) {
        repNeighborsAlike = neighbors;
        repNeighborsEmpty = empty;
        repMaxBabies      = maxBabies;
    }

    /**
     * Sets the reproduction requirements of this Lifeform.
     * @param neighbors    minimum number of same species nearby
     * @param empty        minimum empty Nodes nearby
     * @param maxBabies    that can be reproduced
     * @param food         minimum food sources nearby
     */
    protected void setSexConditions(final int neighbors, final int empty,
                                    final int maxBabies, final int food) {
        setSexConditions(neighbors, empty, maxBabies);
        repNeighborsFood = food;
    }

    /**
     * Sets the inhabitable Terrain for this Animal.
     * @param terrain    that is inhabitable for this Animal
     */
    protected void setInhabitable(final Terrain terrain) {
        inhabitable = terrain;
    }

    /**
     * Sets the Trait that Animal looks for in a target.
     * @param trait    of preys
     */
    protected void setTargetTrait(final Trait trait) {
        targetTrait = trait;
    }

    /**
     * Set the vision level of this Animal.
     * @param vl    vision level
     */
    protected void setVisionLevel(final int vl) {
        visionLevel = vl;
    }

    /**
     * Sets the Node that this Lifeform is in.
     * @param node    that Lifeform will move to
     */
    protected void setNode(final Node node) {
        this.node = node;
    }

    /**
     * Adds a Trait to this Lifeform.
     * @param trait    to add
     */
    protected void addTrait(final Trait trait) {
        traits.add(trait);
    }

    /**
     * Removes Trait from this Lifeform.
     * @param trait    to remove
     */
    protected void removeTrait(final Trait trait) {
        traits.remove(trait);
    }

    /**
     * Sets the Color of this Lifeform.
     */
    protected void setColor(final Color color) {
        this.color = color;
    }

    /**
     * Sets the default Color of this Lifeform.
     */
    protected void setDefaultColor(final Color color) {
        this.defaultColor = color;
        setColor(color);
    }

    /**
     * Sets the health of this Lifeform.
     * @param health    for this Lifeform
     */
    protected void setHealth(final int health) {
        this.health = health;
    }

    /**
     * Sets the max health of this Lifeform.
     * @param health    max health for this Lifeform
     */
    protected void setMaxHealth(final int health) {
        this.maxHealth = health;
        setHealth(health);
    }

    /**
     * Kills this Lifeform.
     */
    protected void kill() {
        alive = false;
    }

    /*
     * Darkens Lifeform's color.
     */
    private void darkenColor() {
        final float fraction = Math.min(1, (float) getHealth() / getMaxHealth());
        final int r = (int) Math.round(Math.max(0, getDefaultColor().getRed() * fraction));
        final int g = (int) Math.round(Math.max(0, getDefaultColor().getGreen() * fraction));
        final int b = (int) Math.round(Math.max(0, getDefaultColor().getBlue() * fraction));
        setColor(new Color(r, g, b, getDefaultColor().getAlpha()));
    }

    /*
     * Makes babies at random empty Nodes
     */
    private Lifeform[] makeBabies(final Iterator<Node> empty, int maxBabies) {
        final List<Lifeform> newborns = new ArrayList<Lifeform>();

        while (empty.hasNext() && maxBabies > 0) {
            Node n = empty.next();
            Lifeform lf;

            if (n.getTerrain().equals(getInhabitable())) {
                continue;
            }

            lf = getWorld().getSpawn().spawnAt(n, getLifeformType());
            newborns.add(lf);

            empty.remove();
            --maxBabies;
        }

        return newborns.toArray(new Lifeform[newborns.size()]);
    }
}

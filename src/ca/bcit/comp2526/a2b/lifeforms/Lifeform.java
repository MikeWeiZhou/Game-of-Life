package ca.bcit.comp2526.a2b.lifeforms;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Node;
import ca.bcit.comp2526.a2b.grids.Terrain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Lifeform.
 *
 * @author  Wei Zhou
 * @version 2016-11-19
 * @since   2016-11-06
 */
public abstract class Lifeform {

    public static final Terrain FOUNTAIN_OF_YOUTH = Terrain.WATER;

    // mandatory settings to set
    private final World        world;
    private final Set<Trait>   traits;
    private final LifeformType type;
    private       Node         location;
    private       boolean      alive;
    private       Color        color;
    private       Color        defaultColor;
    private       int          health;
    private       int          maxHealth;
    private       float        mortalityRate;
    private       int          movement;

    // mandatory: reproduction settings
    private       int          repNeighborsAlike;
    private       int          repNeighborsEmpty;
    private       int          repMaxBabies;
    private       int          repNeighborsFood;

    // optional settings to set
    private       Trait        targetTrait;
    private       Terrain      inhabitable;

    /**
     * Constructs a new Lifeform.
     * @param lft         LifeformType
     * @param location    that contains this Lifeform
     * @param world       that contains this Lifeform
     */
    public Lifeform(final LifeformType lft, final Node location, final World world) {
        this.traits   = new HashSet<Trait>();
        this.type     = lft;
        this.location = location;
        this.world    = world;
        this.alive    = true;
    }

    /**
     * Initializes Lifeform. Throws an error if Lifeform is in an illegal state.
     */
    public void init() {
        if (getDefaultColor() == null || getColor() == null) {
            throw new IllegalStateException("Failed to initialize Lifeform: color or default "
                    + "color not set");
        }
        if (getMaxHealth() == 0 || getHealth() == 0) {
            throw new IllegalStateException("Failed to initialize Lifeform: health or max health "
                    + "not set");
        }
        if (getMortalityRate() == 0) {
            throw new IllegalStateException("Failed to initialize Lifeform: mortality rate must "
                    + "be greater than 0");
        }
        if (repNeighborsAlike == 0 || repNeighborsEmpty == 0 || repMaxBabies == 0) {
            throw new IllegalStateException("Failed to initialize Lifeform: reproduction settings"
                    + " not valid");
        }

        getLocation().setInhabitant(this);
    }

    // ---------------------------------------- TAKE TURN ------------------------------------------

    /**
     * Take action.
     */
    public void takeAction() {
        age();
    }

    /**
     * Returns an array of new Lifeforms on successful reproduction, or an empty array.
     * @return array of Lifeforms
     */
    public Lifeform[] reproduce() {
        final List<Node> emptyNodes = new ArrayList<Node>();
        final Node[]     nearby     = getLocation().getImmediateNeighbors();

        int partnersNearby = 0;
        int emptyNearby    = 0;
        int foodNearby     = 0;

        // determine conditions
        for (Node n : nearby) {

            if (!n.hasLivingInhabitant()) {
                emptyNearby++;
                emptyNodes.add(n);
            } else {
                Lifeform lf = n.getInhabitant();

                if (lf.getLifeformType().equals(getLifeformType())) {
                    partnersNearby++;
                } else if (lf.hasTrait(getTargetTrait())) {
                    foodNearby++;
                }
            }
        }

        if (partnersNearby >= repNeighborsAlike
                && emptyNearby >= repNeighborsEmpty
                && foodNearby >= repNeighborsFood) {

            final int numOfBabies = getRandom().nextInt(repMaxBabies) + 1;
            return makeBabies(emptyNodes.iterator(), numOfBabies);
        }

        return new Lifeform[0]; // no newborns
    }

    /**
     * Kills this Lifeform.
     */
    protected void kill() {
        alive = false;
    }

    /**
     * Kills this Lifeform if health is down to zero, or cancer develops and it dies.
     * If it survives, age its color too.
     */
    protected void age() {
        final float   randFloat;
        final boolean mortalized;

        if (!getLocation().getTerrain().equals(FOUNTAIN_OF_YOUTH)) {
            setHealth(getHealth() - 1);
        }

        randFloat  = getRandom().nextFloat();
        mortalized = 0 <= randFloat && randFloat <= getMortalityRate();
        
        if (getHealth() <= 0 || mortalized) {
            kill();
        } else {
            changeColor();
        }
    }

    /**
     * Changes the Color shade for a Lifeform given the current and maximum health.
     */
    protected void changeColor() {
        final float fraction = Math.min(1, (float) getHealth() / getMaxHealth());
        final int r = Math.round(Math.max(0, getDefaultColor().getRed() * fraction));
        final int g = Math.round(Math.max(0, getDefaultColor().getGreen() * fraction));
        final int b = Math.round(Math.max(0, getDefaultColor().getBlue() * fraction));
        setColor(new Color(r, g, b, getDefaultColor().getAlpha()));
    }

    /**
     * Creates and returns an array of newborns for this Lifeform.
     * @param emptyNodes        Iterator for empty Nodes where babies can live in
     * @param maxBabies         to produce of this LifeformType
     * @return array of newborn Lifeforms
     */
    protected Lifeform[] makeBabies(final Iterator<Node> emptyNodes, int maxBabies) {
        final List<Lifeform> newborns = new ArrayList<Lifeform>();

        while (emptyNodes.hasNext() && maxBabies > 0) {
            final Node location = emptyNodes.next();

            if (location.getTerrain().equals(getInhabitable())) {
                continue;
            }

            if (reproduceAt(location)) {
                newborns.add(getNewborn());
            }

            --maxBabies;
        }

        return newborns.toArray(new Lifeform[newborns.size()]);
    }

    /**
     * Returns true if newborn successfully survives birth, false otherwise.
     * @param location    Node
     * @return Returns true if newborn survives birth
     */
    protected boolean reproduceAt(final Node location) {
        return world.getSpawn().spawnAt(location, getLifeformType());
    }

    /**
     * Returns newborn Lifeform.
     * @return newborn Lifeform
     */
    protected Lifeform getNewborn() {
        return world.getSpawn().getNewborn();
    }

    // ----------------------------------------- SETTERS -------------------------------------------

    /**
     * Sets the mortality rate for this Lifeform.
     * @param rate    mortality rate
     */
    public void setMortalityRate(final float rate) {
        mortalityRate = rate;
    }

    /**
     * Adds a Trait to this Lifeform.
     * @param trait    to add
     */
    protected void addTrait(final Trait trait) {
        traits.add(trait);
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
        repNeighborsAlike = neighbors;
        repNeighborsEmpty = empty;
        repMaxBabies      = maxBabies;
        repNeighborsFood  = food;
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
     * Set the max movement of this Animal.
     * @param movement    max movement
     */
    protected void setMovement(final int movement) {
        this.movement = movement;
    }

    /**
     * Sets the Node that this Lifeform is in.
     * @param newLocation    that Lifeform will move to
     */
    protected void setLocation(final Node newLocation) {
        location = newLocation;
    }

    /**
     * Sets the Color of this Lifeform.
     * @param color    of this Lifeform
     */
    protected void setColor(final Color color) {
        this.color = color;
    }

    /**
     * Sets the default Color of this Lifeform.
     * @param color    default Color of this Lifeform
     */
    protected void setDefaultColor(final Color color) {
        defaultColor = color;
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
        maxHealth = health;
    }

    // ------------------------------------------ GETTERS ------------------------------------------

    /**
     * Return the World Random.
     * @return Random
     */
    public Random getRandom() {
        return world.getRandom();
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
     * Returns true if this Lifeform is alive.
     * @return boolean
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Returns inhabitable Terrain for this Animal.
     * @return inhabitable Terrain for this Lifeform
     */
    public Terrain getInhabitable() {
        return inhabitable;
    }

    /**
     * Returns the Node that this Lifeform is in.
     * @return Node
     */
    public Node getLocation() {
        return location;
    }

    /**
     * Returns the health of this Lifeform.
     * @return health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Returns the max health for this Lifeform.
     * @return max health
     */
    public int getMaxHealth() {
        return maxHealth;
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
     * @return target Trait for this Lifeform
     */
    public Trait getTargetTrait() {
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
     * Returns the natural death ratio of this Lifeform.
     * @return natural death ratio
     */
    public float getMortalityRate() {
        return mortalityRate;
    }

    /**
     * Returns the max movement for this animal.
     * @return maximum movement/steps per turn
     */
    public int getMovement() {
        return movement;
    }
}

package ca.bcit.comp2526.a2b.spawns;

import ca.bcit.comp2526.a2b.Factory;
import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Node;
import ca.bcit.comp2526.a2b.grids.Terrain;
import ca.bcit.comp2526.a2b.lifeforms.Carnivore;
import ca.bcit.comp2526.a2b.lifeforms.Herbivore;
import ca.bcit.comp2526.a2b.lifeforms.Lifeform;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;
import ca.bcit.comp2526.a2b.lifeforms.Omnivore;
import ca.bcit.comp2526.a2b.lifeforms.Plant;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Spawn.
 *
 * @author  Wei Zhou
 * @version 2016-11-19
 * @since   2016-11-06
 */
public abstract class Spawn {

    private static final Map<LifeformType, Class<? extends Lifeform>> CLASSES;

    static {
        CLASSES = new HashMap<LifeformType, Class<? extends Lifeform>>();
        CLASSES.put(LifeformType.PLANT,     Plant.class);
        CLASSES.put(LifeformType.HERBIVORE, Herbivore.class);
        CLASSES.put(LifeformType.OMNIVORE,  Omnivore.class);
        CLASSES.put(LifeformType.CARNIVORE, Carnivore.class);
    }

    private final World                        world;
    private final Random                       random;

    // Lifeforms
    private final TreeMap<Float, LifeformType> spawnRate;
    private       float                        spawnIndex;
    private final Map<LifeformType, Float>     mortalityRates;

    // Terrains
    private final TreeMap<Float, Terrain>      terraformRate;
    private       float                        terraformIndex;
    private       Terrain                      unspawnableTerrain;
    private       Terrain                      convergingTerrain;
    private       float                        convergingRate;

    // Misc
    private       Lifeform                     newborn;
    private       boolean                      variablesLocked;

    /**
     * Constructs a Spawn.
     * @param world    spawns new Lifeform into this World
     */
    public Spawn(final World world) {
        this.world     = world;
        random         = world.getRandom();

        spawnRate      = new TreeMap<Float, LifeformType>();
        spawnIndex     = 0f;
        mortalityRates = new HashMap<LifeformType, Float>();

        terraformRate  = new TreeMap<Float, Terrain>();
        terraformIndex = 0f;

        variablesLocked = false;
    }

    /**
     * Throws an error if Spawn in an illegal state. Then disables modifications to variables.
     */
    public void init() {
        if (spawnRate.size() != mortalityRates.size()) {
            throw new IllegalStateException("Failed to initialize Spawn: # of spawn rate "
                    + "lifeforms must equal to # of mortality rates");
        }
        if (spawnIndex == 0) {
            throw new IllegalStateException("Failed to initialize Spawn: no Lifeform spawn rates "
                    + "added");
        }
        if (spawnIndex > 1.0f) {
            throw new IllegalStateException("Failed to initialize Spawn: spawn rates cannot sum "
                    + "up to over 100%");
        }
        if (terraformIndex != 1.0f) {
            throw new IllegalStateException("Failed to initialize Spawn: terrain distribution "
                    + "does not equate to 100%");
        }
        if (convergingRate > 0 && convergingTerrain == null) {
            throw new IllegalStateException("Failed to initialize Spawn: converging Terrain not "
                    + "set");
        }

        addSpawnRate(null, 1.0f); // fill in spawn rates in case they don't sum up to 100%
        variablesLocked = true;
    }

    // ------------------------------- TERRAFORMING & SPAWNING -------------------------------------

    /**
     * Terraforms the specified Node into a random Terrain based on selected probabilities.
     * @param location    Node
     */
    public void terraformAt(final Node location) {
        if (location == null) {
            return;
        }

        Terrain terrain   = terraformRate.get(terraformRate.lowerKey(random.nextFloat()));
        Node[]  neighbors = location.getImmediateNeighbors();

        if (convergingRate > 0) {
            float convergeProbability;
            int   waterNeighborCount;

            waterNeighborCount = 0;
            for (Node neighbor : neighbors) {
                Terrain neighborTerrain = neighbor.getTerrain();
                if (neighborTerrain != null && neighborTerrain.equals(convergingTerrain)) {
                    waterNeighborCount++;
                }
            }

            convergeProbability = convergingRate * ((float) waterNeighborCount / neighbors.length);
            if (waterNeighborCount > 0 && random.nextFloat() <= convergeProbability) {
                terrain = convergingTerrain;
            }
        }

        location.setTerrain(terrain);
    }

    /**
     * Returns true if newborn successfully lives thru birth, false otherwise.
     * @param node    to spawnAt Lifeform in
     * @return true if Lifeform lived thru birth
     */
    public boolean spawnAt(final Node node) {
        if (node == null) {
            return false;
        }

        final float        randomKey;
        final LifeformType lft;

        randomKey = spawnRate.lowerKey(random.nextFloat());
        lft       = spawnRate.get(randomKey);
        return spawnAt(node, lft);
    }

    /**
     * Returns true if newborn successfully lives thru birth, false otherwise.
     * @param node     that Lifeform spawns in
     * @param lft      LifeformType to create
     * @return true if Lifeform lived thru birth
     */
    public boolean spawnAt(final Node node, final LifeformType lft) {
        if (node == null || lft == null) {
            return false;
        }

        final Terrain    terrain    = node.getTerrain();
        final Class<?>[] paramTypes = {Node.class, World.class};
        final Object[]   paramVals  = {node, world};
        final Lifeform   lf         = Factory.createObject(CLASSES.get(lft), paramTypes, paramVals);

        if (!terrain.equals(unspawnableTerrain) && !terrain.equals(lf.getInhabitable())) {
            lf.setMortalityRate(mortalityRates.get(lft));
            lf.init();
            setNewborn(lf);
            return true;
        }

        return false;
    }

    /**
     * Returns newborn Lifeform, or null.
     * @return newborn Lifeform or null
     */
    public Lifeform getNewborn() {
        final Lifeform ret = newborn;
        setNewborn(null);
        return ret;
    }

    /*
     * Sets newborn Lifeform as the specified newborn.
     * @param newborn    Lifeform
     */
    private void setNewborn(final Lifeform newborn) {
        this.newborn = newborn;
    }

    // ----------------------------------------- SETTERS -------------------------------------------

    /**
     * Add the specified terrain terraform probability for the specified Terrain.
     * @param terrain        type
     * @param probability    of a Node being the specified Terrain type
     */
    protected void addTerraformRate(final Terrain terrain, final float probability) {
        if (variablesLocked) {
            return;
        }

        terraformRate.put(terraformIndex, terrain);
        terraformIndex += probability;
    }

    /**
     * Add the specified spawnAt probability for the specified Lifeform.
     * @param lft            LifeformType
     * @param probability    of spawning
     */
    protected void addSpawnRate(final LifeformType lft, final float probability) {
        if (variablesLocked) {
            return;
        }

        spawnRate.put(spawnIndex, lft);
        spawnIndex += probability;
    }

    /**
     * Adds mortalityRate to specified LifeformType.
     * @param lft     LifeformType
     * @param rate    mortality rate
     */
    protected void addMortalityRate(final LifeformType lft, final float rate) {
        if (variablesLocked) {
            return;
        }

        mortalityRates.put(lft, rate);
    }

    /**
     * Disallows spawning Lifeform on the specified Terrain.
     * @param terrain    that cannot give birth to anything
     */
    protected void setUnspawnableTerrain(final Terrain terrain) {
        if (variablesLocked) {
            return;
        }

        unspawnableTerrain = terrain;
    }

    /**
     * Sets the water convergence rate. (WARNING: exponential growth of Terrain.WATER once set)
     * @param terrain    that Water Terrain converges at
     * @param rate       that Water Terrain converges at
     */
    protected void setConvergingTerrain(final Terrain terrain, final float rate) {
        if (variablesLocked) {
            return;
        }

        convergingTerrain = terrain;
        convergingRate    = rate;
    }
}

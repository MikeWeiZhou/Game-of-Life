package ca.bcit.comp2526.a2b.spawns;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Node;
import ca.bcit.comp2526.a2b.grids.Terrain;
import ca.bcit.comp2526.a2b.lifeforms.Carnivore;
import ca.bcit.comp2526.a2b.lifeforms.Herbivore;
import ca.bcit.comp2526.a2b.lifeforms.Lifeform;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;
import ca.bcit.comp2526.a2b.lifeforms.Omnivore;
import ca.bcit.comp2526.a2b.lifeforms.Plant;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Spawn.
 *
 * @author  Wei Zhou
 * @version 2016-11-16
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
    private final TreeMap<Float, Terrain>      terraformRate;
    private final TreeMap<Float, LifeformType> spawnRate;
    private final Map<LifeformType, Float>     mortalityRates;
    private       float                        terraformIndex;
    private       float                        spawnIndex;
    private       Terrain                      unspawnableTerrain;
    private       Terrain                      convergingTerrain;
    private       float                        convergingRate;

    /**
     * Constructs a Spawn.
     * @param world    spawns new Lifeform into this World
     */
    public Spawn(final World world) {
        this.world     = world;
        random         = world.getRandom();
        terraformRate  = new TreeMap<Float, Terrain>();
        spawnRate      = new TreeMap<Float, LifeformType>();
        mortalityRates = new HashMap<LifeformType, Float>();
        terraformIndex = 0f;
        spawnIndex     = 0f;
    }

    /**
     * Throws an error if Spawn in an illegal state. Then finalizes spawn rates.
     */
    public void init() {
        if (spawnRate.size() != mortalityRates.size() || terraformIndex != 1f) {
            throw new IllegalStateException();
        }

        addSpawnRate(null, 1.0f);
    }

    /**
     * Disallows spawning Lifeform on the specified Terrain.
     * @param terrain    that cannot give birth to anything
     */
    protected void setUnspawnableTerrain(final Terrain terrain) {
        unspawnableTerrain = terrain;
    }

    /**
     * Sets the water convergence rate. (WARNING: exponential growth of Terrain.WATER once set)
     * @param terrain    that Water Terrain converges at
     * @param rate       that Water Terrain converges at
     */
    protected void setConvergingTerrain(final Terrain terrain, final float rate) {
        convergingTerrain = terrain;
        convergingRate    = rate;
    }

    /**
     * Add the specified terrain terraform probability for the specified Terrain.
     * @param terrain        type
     * @param probability    of a Node being the specified Terrain type
     */
    protected void addTerraformRate(final Terrain terrain, final float probability) {
        terraformRate.put(terraformIndex, terrain);
        terraformIndex += probability;
    }

    /**
     * Add the specified spawnAt probability for the specified Lifeform.
     * @param lft            LifeformType
     * @param probability    of spawning
     */
    protected void addSpawnRate(final LifeformType lft, final float probability) {
        spawnRate.put(spawnIndex, lft);
        spawnIndex += probability;
    }

    /**
     * Adds mortalityRate to specified LifeformType.
     * @param lft     LifeformType
     * @param rate    mortality rate
     */
    protected void addMortalityRate(final LifeformType lft, final float rate) {
        mortalityRates.put(lft, rate);
    }

    /**
     * Terraforms the specified Node into a random Terrain based on selected probabilities.
     * @param location    Node
     */
    public void terraformAt(final Node location) {
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
     * Creates a Lifeform based on selected probabilities at the specified Node.
     * @param node    to spawnAt Lifeform in
     * @return Lifeform or null
     */
    public Lifeform spawnAt(final Node node) {
        final float        randomKey;
        final LifeformType lft;

        randomKey = spawnRate.lowerKey(random.nextFloat());
        lft       = spawnRate.get(randomKey);
        return spawnAt(node, lft);
    }

    /**
     * Creates and returns a specified Lifeform or null.
     * @param node     that Lifeform spawns in
     * @param lft      LifeformType to create
     * @return Lifeform or null
     */
    public Lifeform spawnAt(final Node node, final LifeformType lft) {
        if (node == null || lft == null) {
            return null;
        }

        try {
            final Terrain terrain = node.getTerrain();
            final Constructor constructor = CLASSES.get(lft)
                    .getConstructor(Node.class, World.class);
            final Lifeform lf = (Lifeform) constructor.newInstance(node, world);


            if (!terrain.equals(unspawnableTerrain) && !terrain.equals(lf.getInhabitable())) {
                lf.setMortalityRate(mortalityRates.get(lft));
                lf.init();
                return lf;
            }
        } catch (final InstantiationException ex) {
            System.err.println("Error creating: " + lft);
            System.exit(1);
        } catch (final IllegalAccessException ex) {
            System.err.println(lft + " must have a public constructor");
            System.exit(1);
        } catch (final NoSuchMethodException ex) {
            System.err.println("No such constructor in creating: " + lft);
            System.exit(1);
        } catch (final InvocationTargetException ex) {
            System.err.println("Error in invoking class: " + lft);
            System.exit(1);
        }

        return null;
    }
}

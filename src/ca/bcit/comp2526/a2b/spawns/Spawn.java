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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Spawn.
 *
 * @author  Wei Zhou
 * @version 2016-11-20
 * @since   2016-11-06
 */
public abstract class Spawn {

    private static final float TERRAIN_CONVERGENCE_FACTOR;
    private static final Map<LifeformType, Class<? extends Lifeform>> CLASSES;

    static {
        // probability of Terrain convergence (0f - 0.99f)
        TERRAIN_CONVERGENCE_FACTOR = .99f;

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
    private       Lifeform                     newborn;

    // Terrains
    private final TreeMap<Terrain, Float>      terraformRate;
    private       Terrain                      unspawnableTerrain;
    // Tracks Terraform Process
    private       int                          terraformCounter;
    private       Terrain                      terraformTerrain;

    // Misc
    private       boolean                      variablesLocked;

    /**
     * Constructs a Spawn.
     * @param world    spawns new Lifeform into this World
     */
    public Spawn(final World world) {
        this.world     = world;
        random         = world.getRandom();

        // Lifeforms
        spawnRate      = new TreeMap<Float, LifeformType>();
        spawnIndex     = 0f;
        mortalityRates = new HashMap<LifeformType, Float>();

        // Terrain
        terraformRate  = new TreeMap<Terrain, Float>();

        // Misc
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
        checkTerraformRates();

        addSpawnRate(null, 1.0f); // fill in spawn rates in case they don't sum up to 100%
        variablesLocked = true;
    }

    /*
     * Throws error if terraformRates does not sum up to 100%.
     */
    private void checkTerraformRates() {
        float terraformRateSum = 0;
        for (Map.Entry<Terrain, Float> entry : terraformRate.entrySet()) {
            terraformRateSum += entry.getValue();
        }
        if (terraformRateSum != 1.0f) {
            throw new IllegalStateException("Failed to initialize Spawn: terrain distribution "
                    + "does not equate to 100%");
        }
    }

    // ------------------------------------ TERRAFORMING -------------------------------------------

    /**
     * Terraforms all Nodes in the Grid.
     */
    public void terraform() {
        final List<Node> allNodes = getAllNodes();
        Collections.shuffle(allNodes);

        // terraform one type of Terrain at a time
        for (Map.Entry<Terrain, Float> entry : terraformRate.entrySet()) {
            Iterator<Node> barren = allNodes.iterator();
            terraformTerrain = entry.getKey();
            terraformCounter = Math.round(
                    entry.getValue() * world.getGrid().getCols() * world.getGrid().getRows()
            );

            // attempt to terraform barren Node
            while (barren.hasNext() && terraformCounter > 0) {
                Node node = barren.next();
                if (node.hasTerrain()) {
                    barren.remove();
                    continue;
                }
                terraformAt(node, 1.0f);
            }
        }

        // there may be left over Nodes without a Terrain due to rounding issues
        for (Node node : allNodes) {
            if (node.hasTerrain()) {
                continue;
            }
            node.setTerrain(terraformTerrain);
        }
    }

    /*
     * Terraforms the current Node and subsequent neighboring Nodes recursively.
     * @param location       Node
     * @param probability    of terraforming
     */
    private void terraformAt(final Node location, final float probability) {
        final Node[]  neighbors;
        final boolean chance;
        int neighborsAlike = 0;

        // count neighbors with same Terrain
        neighbors = location.getImmediateNeighbors();
        for (Node neighbor : neighbors) {
            if (neighbor.hasTerrain() && neighbor.getTerrain().equals(terraformTerrain)) {
                neighborsAlike++;
            }
        }

        // terraform if probability is right and we didn't exceed #s for this type of Terrain
        chance = random.nextFloat() <= (probability + (neighborsAlike / neighbors.length));
        if (chance && terraformPermitted()) {
            location.setTerrain(terraformTerrain);
            for (Node neighbor : neighbors) {
                if (!neighbor.hasTerrain()) {
                    terraformAt(neighbor, probability * TERRAIN_CONVERGENCE_FACTOR);
                }
            }
        }
    }

    /*
     * Generates and returns all Nodes from the Grid.
     * @return list of Nodes
     */
    private List<Node> getAllNodes() {
        final List<Node> nodes = new ArrayList<Node>();
        for (int row = 0; row < world.getGrid().getRows(); row++) {
            for (int col = 0; col < world.getGrid().getCols(); col++) {
                nodes.add(world.getGrid().getNodeAt(row, col));
            }
        }
        return nodes;
    }

    /*
     * Returns true if terraforming is permitted for the current terraformTerrain.
     * @return boolean
     */
    private boolean terraformPermitted() {
        return terraformCounter-- > 0;
    }

    // ---------------------------------------- SPAWNING -------------------------------------------

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
        terraformRate.put(terrain, probability);
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
}

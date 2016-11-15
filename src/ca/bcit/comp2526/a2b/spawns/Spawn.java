package ca.bcit.comp2526.a2b.spawns;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Node;
import ca.bcit.comp2526.a2b.grids.Terrain;
import ca.bcit.comp2526.a2b.lifeforms.Lifeform;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Spawn.
 *
 * @author  Wei Zhou
 * @version 2016-11-13
 * @since   2016-11-06
 */
public abstract class Spawn {

    private static final String PACKAGE_NAME;
    private static final Map<LifeformType, String> CLASS_NAMES;

    static {
        PACKAGE_NAME = "ca.bcit.comp2526.a2b.lifeforms";

        CLASS_NAMES = new HashMap<LifeformType, String>();
        CLASS_NAMES.put(LifeformType.PLANT,     "Plant");
        CLASS_NAMES.put(LifeformType.HERBIVORE, "Herbivore");
        CLASS_NAMES.put(LifeformType.OMNIVORE,  "Omnivore");
        CLASS_NAMES.put(LifeformType.CARNIVORE, "Carnivore");
    }

    private final World                        world;
    private final Random                       random;
    private final TreeMap<Float, Terrain>      terraformRate;
    private final TreeMap<Float, LifeformType> spawnRate;
    private       float                        terraformIndex;
    private       float                        spawnIndex;

    /**
     * Constructs a Spawn.
     * @param world    spawns new Lifeform into this World
     */
    public Spawn(final World world) {
        this.world     = world;
        random         = world.getRandom();
        terraformRate  = new TreeMap<Float, Terrain>();
        spawnRate      = new TreeMap<Float, LifeformType>();
        terraformIndex = 0f;
        spawnIndex     = 0f;
    }

    /**
     * Finalizes spawn rates.
     */
    public void init() {
        addTerraformRate(null, 1.0f);
        addSpawnRate(null, 1.0f);
    }

    /**
     * Returns a Terrain based on selected probabilities.
     */
    public Terrain getTerrain() {
        final float randomKey = terraformRate.lowerKey(random.nextFloat());
        return terraformRate.get(randomKey);
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
        final Constructor constructor;
        final Lifeform    lf;

        if (lft == null) {
            return null;
        }

        try {
            constructor = Class.forName(PACKAGE_NAME + "." + CLASS_NAMES.get(lft))
                    .getConstructor(Node.class, World.class);
            lf = (Lifeform) constructor.newInstance(node, world);

            if (!node.getTerrain().equals(lf.getInhabitable())) {
                return lf;
            }
        } catch (final ClassNotFoundException ex) {
            System.err.println("Cannot find class: " + CLASS_NAMES.get(lft));
            System.exit(1);
        } catch (final InstantiationException ex) {
            System.err.println("Error creating: " + CLASS_NAMES.get(lft));
            System.exit(1);
        } catch (final IllegalAccessException ex) {
            System.err.println(CLASS_NAMES.get(lft) + " must have a public constructor");
            System.exit(1);
        } catch (final NoSuchMethodException ex) {
            System.err.println("No such method in creating: " + CLASS_NAMES.get(lft));
            System.exit(1);
        } catch (final InvocationTargetException ex) {
            System.err.println("Error in creating constructor for class: "
                    + CLASS_NAMES.get(lft));
            System.exit(1);
        }

        return null;
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
}

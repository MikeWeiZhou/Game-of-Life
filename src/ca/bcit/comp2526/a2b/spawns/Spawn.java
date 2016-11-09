package ca.bcit.comp2526.a2b.spawns;

import ca.bcit.comp2526.a2b.grids.Node;
import ca.bcit.comp2526.a2b.lifeforms.Lifeform;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;

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
 * @version 2016-11-08
 * @since   2016-11-06
 */
public abstract class Spawn {

    private static final Map<LifeformType, String> classNames;
    private static final String packageName;
    private static final Random random;

    static {
        classNames  = new HashMap<LifeformType, String>();
        packageName = "ca.bcit.comp2526.a2b.lifeforms";
        random      = new Random();

        classNames.put(LifeformType.LIFELESS,  "Lifeless");
        classNames.put(LifeformType.PLANT,     "Plant");
        classNames.put(LifeformType.HERBIVORE, "Herbivore");
    }

    private final   TreeMap<Float, LifeformType> spawnRate;
    private float   spawnIndex;

    /**
     * Constructs a Spawn.
     */
    public Spawn() {
        spawnRate  = new TreeMap<Float, LifeformType>();
        spawnIndex = 0f;
    }

    /**
     * Creates a Lifeform based on selected probabilities at the specified Node.
     * @param node    to spawn Lifeform in
     * @return Lifeform
     */
    public Lifeform spawnAt(final Node node) {
        final float        rand = spawnRate.lowerKey(random.nextFloat());
        final LifeformType lft = spawnRate.get(rand);
        return spawn(lft, node);
    }

    /**
     * Creates and returns a specified Lifeform.
     * @param lft     LifeformType to create
     * @param node    that Lifeform spawns in
     * @return Lifeform
     */
    public static Lifeform spawn(final LifeformType lft, final Node node) {
        final Constructor constructor;

        try {
            constructor = Class.forName(packageName + "." + classNames.get(lft))
                    .getConstructor(Node.class);
            return (Lifeform) constructor.newInstance(node);
        } catch (final ClassNotFoundException ex) {
            System.err.println("Cannot find class: " + classNames.get(lft));
            System.exit(1);
        } catch (final InstantiationException ex) {
            System.err.println("Error creating: " + classNames.get(lft));
            System.exit(1);
        } catch (final IllegalAccessException ex) {
            System.err.println(classNames.get(lft) + " must have a public constructor");
            System.exit(1);
        } catch (final NoSuchMethodException ex) {
            System.err.println("No such method in creating: " + classNames.get(lft));
            System.exit(1);
        } catch (final InvocationTargetException ex) {
            System.err.println("Error in creating constructor for class: "
                    + classNames.get(lft));
            System.exit(1);
        }

        // Should be unreachable!
        return null;
    }

    /**
     * Add the specified spawn probability for the specified Lifeform.
     * @param lft            LifeformType
     * @param probability    of spawning
     */
    protected void addSpawnRate(final LifeformType lft, final float probability) {
        spawnRate.put(spawnIndex, lft);
        spawnIndex += probability;
    }
}

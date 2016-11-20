package ca.bcit.comp2526.a2b;

import ca.bcit.comp2526.a2b.grids.Grid;
import ca.bcit.comp2526.a2b.grids.GridType;
import ca.bcit.comp2526.a2b.grids.SquareGrid;
import ca.bcit.comp2526.a2b.renderers.Renderer;
import ca.bcit.comp2526.a2b.renderers.SquareRenderer;
import ca.bcit.comp2526.a2b.spawns.AllIDoIsEat;
import ca.bcit.comp2526.a2b.spawns.NaturalSpawn;
import ca.bcit.comp2526.a2b.spawns.OmnivoresOnly;
import ca.bcit.comp2526.a2b.spawns.Spawn;
import ca.bcit.comp2526.a2b.spawns.SpawnType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory.
 *
 * @author  Wei Zhou
 * @version 2016-11-19
 * @since   2016-11-19
 */
public final class Factory {

    private static final Map<GridType, Class<? extends Grid>>     GRID_CLASSES;
    private static final Map<GridType, Class<? extends Renderer>> RENDERER_CLASSES;
    private static final Map<SpawnType, Class<? extends Spawn>>   SPAWN_CLASSES;

    static {
        GRID_CLASSES = new HashMap<GridType, Class<? extends Grid>>();
        GRID_CLASSES.put(GridType.SQUARE, SquareGrid.class);

        RENDERER_CLASSES = new HashMap<GridType, Class<? extends Renderer>>();
        RENDERER_CLASSES.put(GridType.SQUARE, SquareRenderer.class);

        SPAWN_CLASSES = new HashMap<SpawnType, Class<? extends Spawn>>();
        SPAWN_CLASSES.put(SpawnType.NATURAL_SPAWN,   NaturalSpawn.class);
        SPAWN_CLASSES.put(SpawnType.ALL_I_DO_IS_EAT, AllIDoIsEat.class);
        SPAWN_CLASSES.put(SpawnType.OMNIVORES_ONLY,  OmnivoresOnly.class);
    }

    /**
     * Creates a new Grid with specified attributes.
     * @param type      of grid
     * @param rows      in the grid
     * @param cols      in the grid
     * @param length    of each cell/node in grid
     * @return a new Grid
     */
    public static Grid createGrid(final GridType type, final int rows, final int cols,
                                                                       final int length) {

        final Class<?>[]  paramTypes = {int.class, int.class, int.class};
        final Object[]    paramVals  = {rows, cols, length};
        return createObject(GRID_CLASSES.get(type), paramTypes, paramVals);
    }

    /**
     * Creates a new Spawn with the specified type.
     * @param spawnType    to create
     * @param world        that Spawn belongs to
     * @return a new Spawn
     */
    public static Spawn createSpawn(final SpawnType spawnType, final World world) {
        final Class<?>[]  paramTypes = {World.class};
        final Object[]    paramVals  = {world};
        return createObject(SPAWN_CLASSES.get(spawnType), paramTypes, paramVals);
    }

    /**
     * Creates a new Renderer.
     * @param type     of Renderer
     * @param world    that Renderer will render
     * @return a new Renderer
     */
    public static Renderer createRenderer(final GridType type, final World world) {
        final Class<?>[]  paramTypes = {World.class};
        final Object[]    paramVals  = {world};
        return createObject(RENDERER_CLASSES.get(type), paramTypes, paramVals);
    }

    /**
     * Creates a new Object of the given Class with specified parameter types and values.
     * @param clazz         Class to be instantiated
     * @param paramTypes    parameter types for constructor
     * @param paramVals     parameter values for constructor
     * @param <E>           type of Class to be instantiated
     * @return a new instantiation of the specified class
     */
    public static <E> E createObject(final Class<? extends E> clazz, final Class<?>[] paramTypes,
                                                                     final Object[]   paramVals) {
        try {
            @SuppressWarnings("unchecked")
            final Constructor<E> constructor = (Constructor<E>) clazz.getConstructor(paramTypes);
            return constructor.newInstance(paramVals);
        } catch (final NoSuchMethodException ex) {
            System.err.println("Factory: no such constructor in class " + clazz.toString());
            System.exit(1);
        } catch (final InstantiationException ex) {
            System.err.println("Factory: error instantiation class: " + clazz.toString());
            System.exit(1);
        } catch (final IllegalAccessException ex) {
            System.err.println("Factory: no access to constructor in class: " + clazz.toString());
            System.exit(1);
        } catch (final InvocationTargetException ex) {
            System.err.println("Factory: invocation target exception in class " + clazz.toString());
            System.exit(1);
        }

        // should not be reachable
        return null;
    }

    /*
     * Prevent instantiation of Factory.
     */
    private Factory() {}
}

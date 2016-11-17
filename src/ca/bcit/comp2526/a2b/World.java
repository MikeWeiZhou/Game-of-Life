package ca.bcit.comp2526.a2b;

import ca.bcit.comp2526.a2b.grids.Grid;
import ca.bcit.comp2526.a2b.grids.Node;
import ca.bcit.comp2526.a2b.grids.SquareGrid;
import ca.bcit.comp2526.a2b.grids.Terrain;
import ca.bcit.comp2526.a2b.lifeforms.Lifeform;
import ca.bcit.comp2526.a2b.renderers.Renderer;
import ca.bcit.comp2526.a2b.renderers.SquareRenderer;
import ca.bcit.comp2526.a2b.spawns.NaturalSpawn;
import ca.bcit.comp2526.a2b.spawns.Spawn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import javax.swing.JFrame;

/**
 * World.
 *
 * @author  Wei Zhou
 * @version 2016-11-16
 * @since   2016-11-06
 */
public class World {

    private final Random         random;
    private final List<Lifeform> lifeforms;
    private final Grid           grid;
    private final Spawn          spawn;
    private final Renderer       renderer;

    private final List<Long>     timer;
    private       int            turnsTimed;

    /**
     * Constructor.
     */
    public World(final JFrame frame) {
        random    = new Random();
        lifeforms = new ArrayList<Lifeform>();
        grid      = new SquareGrid(65, 40, 10);
        spawn     = new NaturalSpawn(this);
        renderer  = new SquareRenderer(frame, this);

        timer      = new ArrayList<Long>();
        turnsTimed = 0;
    }

    /**
     * Initializes this World.
     */
    public void init() {
        grid.init();
        spawn.init();
        createWorld();   // must be initiated right before renderer.int()
        renderer.init(); // must be initiated last
    }

    /**
     * Takes a turn.
     */
    public void takeTurn() {

        long t1 = 0;
        long t2;

        if (++turnsTimed < 100) {
            t1 = new Date().getTime();
        }

        // all living Lifeforms take action
        ListIterator<Lifeform> it = lifeforms.listIterator();
        while (it.hasNext()) {
            Lifeform lf = it.next();
            if (!lf.isAlive()) {
                continue;
            }

            lf.takeAction();
            Lifeform[] newborns = lf.reproduce();
            for (int i = 0; i < newborns.length; i++) {
                it.add(newborns[i]);
            }
        }

        removeDeadLifeforms();
        Collections.shuffle(lifeforms);
        renderer.update();

        // restart game if no more lifeforms
        if (lifeforms.size() == 0) {
            createWorld();
        }

        if (turnsTimed < 100) {
            t2 = new Date().getTime();
            long avg = t2 - t1;
            timer.add(avg);
        } else {
            long sum = 0;
            for (long time : timer) {
                sum += time;
            }
            long avg = sum / timer.size();
            System.out.println("Average time per turn: " + avg);
        }
    }

    /*
     * Populates the World with Terrain and Lifeforms.
     */
    private void createWorld() {
        for (int row = 0; row < getGrid().getRows(); row++) {
            for (int col = 0; col < getGrid().getCols(); col++) {
                Node location = getGrid().getNodeAt(row, col);
                getSpawn().terraformAt(location); // Node needs Terrain before Lifeform can spawn
                Lifeform lf = getSpawn().spawnAt(location);

                if (lf != null) {
                    lifeforms.add(lf);
                }
            }
        }
    }

    /*
     * Removes all dead Lifeforms.
     */
    private void removeDeadLifeforms() {
        Iterator<Lifeform> it = lifeforms.iterator();
        while (it.hasNext()) {
            if (!it.next().isAlive()) {
                it.remove();
            }
        }
    }

    // ------------------------------------------ GETTERS ------------------------------------------

    /**
     * Returns the Random generator from this World.
     * @return Random
     */
    public Random getRandom() {
        return random;
    }

    /**
     * Returns the Spawn from this World.
     * @return Spawn
     */
    public Spawn getSpawn() {
        return spawn;
    }

    /**
     * Returns the Grid used in this World.
     * @return Grid
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Returns all Lifeforms in this World.
     * @return Lifeforms
     */
    public Lifeform[] getLifeforms() {
        return lifeforms.toArray(new Lifeform[lifeforms.size()]);
    }

    /**
     * Returns the Lifeform at the specified row and column, or null.
     * @param node    to check
     * @return Lifeform or null
     */
    public Lifeform getLifeformAt(final Node node) {
        final Lifeform lf = node.getInhabitant();
        return (lf != null && lf.isAlive()) ? lf : null;
    }
}

package ca.bcit.comp2526.a2b;

import ca.bcit.comp2526.a2b.grids.Grid;
import ca.bcit.comp2526.a2b.grids.GridType;
import ca.bcit.comp2526.a2b.grids.Node;
import ca.bcit.comp2526.a2b.lifeforms.Lifeform;
import ca.bcit.comp2526.a2b.renderers.Renderer;
import ca.bcit.comp2526.a2b.spawns.Spawn;
import ca.bcit.comp2526.a2b.spawns.SpawnType;

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
 * @version 2016-11-19
 * @since   2016-11-06
 */
public class World {

    private static final int ROWS;
    private static final int COLS;
    private static final int NODE_LENGTH;

    static {
        ROWS        = 160;
        COLS        = 80;
        NODE_LENGTH = 6;
    }

    private final JFrame         frame;
    private final Random         random;
    private final List<Lifeform> lifeforms;
    private final Grid           grid;
    private final Spawn          spawn;
    private final Renderer       renderer;

    private final List<Long>     timer;
    private       int            turnsTimed;

    /**
     * Constructor.
     * @param gameFrame    game frame to draw on
     * @param gridType     type of grid
     * @param spawnType    type of spawn
     */
    public World(final JFrame gameFrame, final GridType gridType, final SpawnType spawnType) {
        frame     = gameFrame;
        random    = new Random();
        lifeforms = new ArrayList<Lifeform>();
        grid      = Factory.createGrid(gridType, ROWS, COLS, NODE_LENGTH);
        spawn     = Factory.createSpawn(spawnType, this);
        renderer  = Factory.createRenderer(gridType, this);

        timer      = new ArrayList<Long>();
        turnsTimed = 0;
    }

    /**
     * Initializes this World.
     */
    public void init() {
        if (frame == null) {
            throw new IllegalStateException("World: must have a valid game frame.");
        }

        grid.init();
        spawn.init();
        createWorld();   // must be initiated after grid and spawn
        renderer.init(); // must be initiated after createWorld()
    }

    /*
     * Populates the World with Terrain and Lifeforms.
     */
    private void createWorld() {
        for (int row = 0; row < getGrid().getRows(); row++) {
            for (int col = 0; col < getGrid().getCols(); col++) {
                Node location = getGrid().getNodeAt(row, col);
                getSpawn().terraformAt(location);

                if (getSpawn().spawnAt(location)) {
                    lifeforms.add(getSpawn().getNewborn());
                }
            }
        }
    }

    // ---------------------------------------- TAKE TURN ------------------------------------------

    /**
     * Takes a turn.
     */
    public void takeTurn() {
        long t1 = 0;
        long t2;
        if (++turnsTimed < 250) {
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
            for (Lifeform newborn : newborns) {
                it.add(newborn);
            }
        }

        // must remove dead Lifeforms before rendering,
        // or renders dead Lifeforms too
        removeDeadLifeforms();
        Collections.shuffle(lifeforms);
        renderer.update();

        if (turnsTimed < 250) {
            t2 = new Date().getTime();
            long avg = t2 - t1;
            timer.add(avg);
        } else if (turnsTimed == 250) {
            long sum = 0;
            for (long time : timer) {
                sum += time;
            }
            long avg = sum / timer.size();
            System.out.println("Average time per turn calculations: " + avg + " ms");
            turnsTimed = 0;
            timer.clear();
        }
    }

    /*
     * Removes all dead Lifeforms.
     */
    private void removeDeadLifeforms() {
        final Iterator<Lifeform> lf = lifeforms.iterator();
        while (lf.hasNext()) {
            if (!lf.next().isAlive()) {
                lf.remove();
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
     * Returns the game frame of this World.
     * @return game frame as JFrame
     */
    public JFrame getFrame() {
        return frame;
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
}

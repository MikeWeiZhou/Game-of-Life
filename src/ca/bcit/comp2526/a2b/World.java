package ca.bcit.comp2526.a2b;

import ca.bcit.comp2526.a2b.grids.Grid;
import ca.bcit.comp2526.a2b.grids.GridType;
import ca.bcit.comp2526.a2b.grids.Node;
import ca.bcit.comp2526.a2b.lifeforms.Lifeform;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;
import ca.bcit.comp2526.a2b.renderers.Renderer;
import ca.bcit.comp2526.a2b.spawns.Spawn;
import ca.bcit.comp2526.a2b.spawns.SpawnType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Set;

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

    private static final int MIN_LIFEFORMS;
    private static final int CHECK_GAMEOVER_INTERVAL;

    static {
        ROWS        = 160;
        COLS        = 80;
        NODE_LENGTH = 6;

        MIN_LIFEFORMS           = 5;
        CHECK_GAMEOVER_INTERVAL = 70;
    }

    private final Random         random;
    private final List<Lifeform> lifeforms;
    private final Grid           grid;
    private final Spawn          spawn;
    private final Renderer       renderer;
    private       int            turnsPassed;

    private final List<NotifyWhenGameOver> observers;

    /**
     * Constructor.
     * @param gridType     type of grid
     * @param spawnType    type of spawn
     */
    public World(final GridType gridType, final SpawnType spawnType) {
        random      = new Random();
        lifeforms   = new ArrayList<Lifeform>();
        grid        = Factory.createGrid(gridType, ROWS, COLS, NODE_LENGTH);
        spawn       = Factory.createSpawn(spawnType, this);
        renderer    = Factory.createRenderer(gridType, this);
        turnsPassed = 0;

        observers = new ArrayList<NotifyWhenGameOver>();
    }

    /**
     * Initializes this World.
     */
    public void init() {
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
        if (++turnsPassed == CHECK_GAMEOVER_INTERVAL) {
            checkGameover();
            turnsPassed = 0;
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

    // ----------------------------------------- OBSERVERS -----------------------------------------

    /**
     * Adds observer to the list of observers that will be notified when game is over.
     * @param observer    to be added
     */
    public void notifyWhenGameOver(final NotifyWhenGameOver observer) {
        observers.add(observer);
    }

    /*
     * Notifies all observers if number of Lifeforms cannot support an ecosystem; Game over.
     */
    private void checkGameover() {
        if (lifeforms.size() < MIN_LIFEFORMS) {
            notifyObservers();
        } else {
            final Set<LifeformType> species = new HashSet<LifeformType>();
            for (Lifeform lf : getLifeforms()) {
                if (!species.contains(lf.getLifeformType())) {
                    species.add(lf.getLifeformType());
                }
            }

            if (species.size() <= 1) {
                notifyObservers();
            }
        }
    }

    /*
     * Notify all observers that the game is over.
     */
    private void notifyObservers() {
        for (NotifyWhenGameOver observer : observers) {
            observer.gameover();
        }
    }

    // ------------------------------------------ GETTERS ------------------------------------------

    /**
     * Returns this World's Renderer.
     * @return Renderer
     */
    public Renderer getRenderer() {
        return renderer;
    }

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
}

package ca.bcit.comp2526.a2b;

import ca.bcit.comp2526.a2b.grids.*;
import ca.bcit.comp2526.a2b.lifeforms.Lifeform;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;
import ca.bcit.comp2526.a2b.renderers.Renderer;
import ca.bcit.comp2526.a2b.renderers.SquareRenderer;
import ca.bcit.comp2526.a2b.spawns.AncientPhytophagous;
import ca.bcit.comp2526.a2b.spawns.NaturalSpawn;
import ca.bcit.comp2526.a2b.spawns.Spawn;

import javax.swing.JFrame;
import java.util.*;

/**
 * World.
 *
 * @author  Wei Zhou
 * @version 2016-11-13
 * @since   2016-11-06
 */
public class World {

    private final Random         random;
    private final List<Lifeform> lifeforms;
    private       Grid           grid;
    private       Spawn          spawn;
    private       Renderer       renderer;

    /**
     * Constructor.
     */
    public World(final JFrame frame) {
        random    = new Random();
        lifeforms = new ArrayList<Lifeform>();
        grid      = new SquareGrid(60, 40, 15);
        spawn     = new NaturalSpawn(this);
        renderer  = new SquareRenderer(frame, this);
    }

    /**
     * Initializes this World.
     *
     * <p>
     * <b>Assumes</b> this method is called only after the Grid object is
     * constructed and initialized.
     * </p>
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

        // all alive Lifeforms take action
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

    /**
     * Returns the Lifeform at the specified row and column.
     * @param node    to check
     * @return Lifeform or null
     */
    public Lifeform getLifeformAt(final Node node) {
        for (Lifeform lf : lifeforms) {
            if (lf.getNode() == node && lf.isAlive()) {
                return lf;
            }
        }
        return null;
    }

    /*
     * Populates the World with Terrain and Lifeforms.
     */
    private void createWorld() {
        Node     n;
        Terrain  t;
        Lifeform lf;

        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                n = grid.getNodeAt(row, col);
                t = spawn.getTerrain();
                n.setTerrain(t);

                lf = spawn.spawnAt(n);
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
            Lifeform lf = it.next();
            if (!lf.isAlive()) {
                it.remove();
            }
        }
    }
}

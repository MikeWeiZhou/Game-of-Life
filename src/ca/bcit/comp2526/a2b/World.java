package ca.bcit.comp2526.a2b;

import ca.bcit.comp2526.a2b.grids.Node;
import ca.bcit.comp2526.a2b.lifeforms.Lifeform;
import ca.bcit.comp2526.a2b.grids.Grid;
import ca.bcit.comp2526.a2b.spawns.Spawn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * World.
 *
 * @author  Wei Zhou
 * @version 2016-11-08
 * @since   2016-11-06
 */
public class World {

    private List<Lifeform> lifeforms;
    private Spawn          spawn;
    private Grid           grid;

    /**
     * Constructs a World.
     * @param spawn    that creates Lifeforms
     * @param grid     structure that Lifeforms live in
     */
    public World(final Spawn spawn, final Grid grid) {
        lifeforms  = new ArrayList<Lifeform>();
        this.spawn = spawn;
        this.grid  = grid;
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
        populateWorld();
    }

    /**
     * Takes a turn, from all Lifeforms in random order.
     */
    public void takeTurn() {
        for (Lifeform lf : lifeforms) {
            lf.takeActions();
        }
        Collections.shuffle(lifeforms);
    }

    /**
     * Returns all Lifeforms in this World.
     * @return Lifeforms
     */
    public Lifeform[] getLifeforms() {
        return lifeforms.toArray(new Lifeform[lifeforms.size()]);
    }

    /*
     * Populates the World with Lifeforms.
     */
    private void populateWorld() {
        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                Node n = grid.getNodeAt(row, col);
                lifeforms.add(spawn.spawnAt(n));
            }
        }
    }
}

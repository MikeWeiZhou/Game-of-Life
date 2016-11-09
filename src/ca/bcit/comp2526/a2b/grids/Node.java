package ca.bcit.comp2526.a2b.grids;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Node.
 *
 * @author  Wei Zhou
 * @version 2016-11-08
 * @since   2016-11-06
 */
public class Node {

    private final Map<Integer, Node[]> neighbors;
//    private final Terrain terrain;
    private final Point   point;
    private final int     row;
    private final int     col;

//    /**
//     * Constructs a Node.
//     * @param terrain      of this Node
//     * @param row          of this Node
//     * @param col          of this Node
//     */
//    public Node(final Terrain terrain, final Point point, final int row, final int col) {
//        neighbors    = new HashMap<Integer, Node[]>();
//        this.terrain = terrain;
//        this.point   = point;
//        this.row     = row;
//        this.col     = col;
//    }

    /**
     * Constructs a Node.
     * @param row          of this Node
     * @param col          of this Node
     */
    public Node(final Point point, final int row, final int col) {
        neighbors    = new HashMap<Integer, Node[]>();
        this.point   = point;
        this.row     = row;
        this.col     = col;
    }

    /**
     * Sets the neighboring Nodes.
     * @param lvl   level of neighbors to set
     * @param nb    neighbors of this Node
     */
    public void setNeighborsForLevel(final int lvl, final Node[] nb) {
        neighbors.put(lvl, nb);
    }

    /**
     * Returns neighboring Nodes.
     * @param lvl    level of neighboring Nodes to retrieve
     * @return neighboring Nodes
     */
    public Node[] getNeighborsForLevel(final int lvl) {
        return neighbors.get(lvl);
    }

//    /**
//     * Returns the Terrain of this Node.
//     * @return Terrain
//     */
//    public Terrain getTerrain() {
//        return terrain;
//    }

    /**
     * Returns the Point of this Node.
     * @return the Point
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Returns the row of this Node.
     * @return row
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column of this Node.
     * @return column
     */
    public int getCol() {
        return col;
    }
}

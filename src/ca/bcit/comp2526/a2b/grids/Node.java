package ca.bcit.comp2526.a2b.grids;

import ca.bcit.comp2526.a2b.lifeforms.Lifeform;

import java.awt.Color;
import java.awt.Point;
import java.util.*;

/**
 * Node.
 *
 * @author  Wei Zhou
 * @version 2016-11-15
 * @since   2016-11-06
 */
public class Node {

    private final static Map<Terrain, Color> COLOR;

    static {
        COLOR = new HashMap<Terrain, Color>();
        COLOR.put(Terrain.LAND,  Color.white);
        COLOR.put(Terrain.WATER, Color.blue);
    }

    private final Map<Integer, Node[]> neighbors;
    private final Point   point;
    private final int     row;
    private final int     col;
    private       Terrain terrain;

    /**
     * Constructs a Node.
     * @param row          of this Node
     * @param col          of this Node
     */
    public Node(final Point point, final int row, final int col) {
        this.neighbors      = new HashMap<Integer, Node[]>();
        this.point          = point;
        this.row            = row;
        this.col            = col;
    }

// ------------------------------------------- SETTERS ---------------------------------------------

    /**
     * Sets the neighboring Nodes.
     * @param lvl   level of neighbors to set
     * @param nb    neighbors of this Node
     */
    public void setNeighborsForLevel(final int lvl, final Node[] nb) {
        neighbors.put(lvl, nb);
    }

    /**
     * Sets the Terrain of this Node.
     */
    public void setTerrain(final Terrain terrain) {
        this.terrain = terrain;
    }

// ------------------------------------------- GETTERS ---------------------------------------------

    /**
     * Returns neighboring Nodes.
     * @param lf    Lifeform that is looking around
     * @return neighboring Nodes
     */
    public Node[] getNeighborsFor(final Lifeform lf) {
        return neighbors.get(lf.getMovement());
    }

    /**
     * Returns the Terrain of this Node.
     * @return Terrain
     */
    public Terrain getTerrain() {
        return terrain;
    }

    /**
     * Returns the Color of the Terrain.
     * @return Color
     */
    public Color getColor() {
        return COLOR.get(terrain);
    }

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

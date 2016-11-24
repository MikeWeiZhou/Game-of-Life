package ca.bcit.comp2526.a2b.grids;

import ca.bcit.comp2526.a2b.lifeforms.Lifeform;
import ca.bcit.comp2526.a2b.renderers.Renderable;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Node.
 *
 * @author  Wei Zhou
 * @version 2016-11-20
 * @since   2016-11-06
 */
public class Node implements Renderable {

    private static final Map<Terrain, Color> TERRAIN_COLOR;

    static {
        TERRAIN_COLOR = new HashMap<Terrain, Color>();
        TERRAIN_COLOR.put(Terrain.LAND,  Color.white);
        TERRAIN_COLOR.put(Terrain.WATER, Color.blue);
    }

    private final Map<Integer, Node[]> neighbors;
    private final Point    point;
    private final int      row;
    private final int      col;
    private final int      length;
    private       Terrain  terrain;
    private       Lifeform inhabitant;

    /**
     * Constructs a Node.
     * @param point        x, y coordinates of this Node
     * @param row          of this Node
     * @param col          of this Node
     * @param length       of this Node
     */
    public Node(final Point point, final int row, final int col, final int length) {
        this.neighbors      = new HashMap<Integer, Node[]>();
        this.point          = point;
        this.row            = row;
        this.col            = col;
        this.length         = length;
    }

    // ------------------------------------------- SETTERS -----------------------------------------

    /**
     * Removes inhabiting Lifeform from this Node.
     */
    public void removeInhabitant() {
        inhabitant = null;
    }

    /**
     * Sets the inhabiting Lifeform of this Node.
     * @param lf    Lifeform
     */
    public void setInhabitant(final Lifeform lf) {
        inhabitant = lf;
    }

    /**
     * Sets the Terrain of this Node.
     * @param terrain    for this Node
     */
    public void setTerrain(final Terrain terrain) {
        this.terrain = terrain;
    }

    /**
     * Sets the neighboring Nodes.
     * @param lvl   level of neighbors to set
     * @param nb    neighbors of this Node
     */
    public void setNeighborsForLevel(final int lvl, final Node[] nb) {
        neighbors.put(lvl, nb);
    }

    // ------------------------------------------- GETTERS -----------------------------------------

    /**
     * Returns true if Node has a Terrain.
     * @return true if Node has a Terrain
     */
    public boolean hasTerrain() {
        return terrain != null;
    }

    /**
     * Returns true if Node has a living inhabitant.
     * @return True if Lifeform inhabiting at this Node is alive
     */
    public boolean hasLivingInhabitant() {
        return inhabitant != null && inhabitant.isAlive();
    }

    /**
     * Returns neighboring Nodes.
     * @param lf    Lifeform that is looking around
     * @return neighboring Nodes
     */
    public Node[] getNeighborsWithinWalkingDistanceFor(final Lifeform lf) {
        return neighbors.get(lf.getMovement());
    }

    /**
     * Returns immediate neighboring Nodes.
     * @return neighboring Nodes
     */
    public Node[] getImmediateNeighbors() {
        return neighbors.get(1);
    }

    /**
     * Returns the inhabiting Lifeform of this Node.
     * @return Lifeform
     */
    public Lifeform getInhabitant() {
        return inhabitant;
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
     * @return Terrain Color
     */
    @Override
    public Color getColor() {
        return TERRAIN_COLOR.get(terrain);
    }

    /**
     * Returns the Point of this Node.
     * @return the Point
     */
    @Override
    public Point getPoint() {
        return point;
    }

    @Override
    public int getSize() {
        return length;
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

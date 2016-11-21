package ca.bcit.comp2526.a2b.grids;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * SquareGrid.
 *
 * @author  Wei Zhou
 * @version 2016-11-17
 * @since   2016-11-06
 */
public class SquareGrid extends Grid {

    /**
     * Constructs a Grid with the specified row and column.
     * @param rows      to create
     * @param cols      to create
     * @param length    of Node
     */
    public SquareGrid(final int rows, final int cols, final int length) {
        super(rows, cols, length);
    }

    /**
     * Calculates and returns the size of Grid.
     * @return Dimension
     */
    protected Dimension calcSize(int length) {
        final int width  = length * getRows();
        final int height = length * getCols();
        return new Dimension(width, height);
    }

    /**
     * Creates and returns a new Node at the specified row and column.
     * @param row    that Node is in
     * @param col    that Node is in
     * @return new Node
     */
    protected Node createNodeAt(final int row, final int col) {
        final int x = row * getLength();
        final int y = col * getLength();
        return new Node(new Point(x, y), row, col, getLength());
    }

    /**
     * Calculates and returns all the neighboring Nodes.
     * @param lvl     level of neighbors to calculate
     * @param node    used to calculate neighboring Nodes
     * @return neighboring Nodes
     */
    protected Node[] calcNeighborsForLevel(final int lvl, final Node node) {
        final List<Node> neighbors = new ArrayList<Node>();
        final int        cRow      = node.getRow();
        final int        cCol      = node.getCol();

        for (int row = cRow - lvl; row <= cRow + lvl; row++) {
            for (int col = cCol - lvl; col <= cCol + lvl; col++) {
                if (inBounds(row, col) && getNodeAt(row, col) != node) {
                    neighbors.add(getNodeAt(row, col));
                }
            }
        }

        /* ADDS ONLY THE SPECIFIED LEVEL NEIGHBORS, NOT CUMULATIVE */
        /*Add top and bottom rows of neighboring Nodes
        for (int row = cRow - lvl; row <= cRow + lvl; row += 2 * lvl) {
            for (int col = cCol - lvl; col <= cCol + lvl; col++) {
                if (inBounds(row, col)) {
                    neighbors.add(getNodeAt(row, col));
                }
            }
        }
        // Add remaining side columns of neighboring Nodes
        for (int row = cRow - lvl + 1; row <= cRow + lvl - 1; row++) {
            for (int col = cCol - lvl; col <= cCol + lvl; col += 2 * lvl) {
                if (inBounds(row, col)) {
                    neighbors.add(getNodeAt(row, col));
                }
            }
        }*/

        return neighbors.toArray(new Node[neighbors.size()]);
    }
}

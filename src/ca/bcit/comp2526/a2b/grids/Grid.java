package ca.bcit.comp2526.a2b.grids;

import java.awt.Dimension;

/*
 * TO DO:
 *
 *  - NEED CONFIRMATION: private int border: not needed, since each node stores
 *      the coordinates x & y of each Node already.
 */

/**
 * Grid.
 *
 * @author  Wei Zhou
 * @version 2016-11-17
 * @since   2016-11-06
 */
public abstract class Grid {

    /* Levels of neighboring Nodes to calculate */
    private static final int NEIGHBOR_LEVELS = 2;

    private final Node[][]  nodes;
    private final int       rows;
    private final int       cols;
    private final int       length;
    private final int       border;
    private       Dimension size;

    /**
     * Constructs a Grid with the specified row and column.
     * @param rows      to create
     * @param cols      to create
     * @param length    of Node
     * @param border    size
     */
    public Grid(final int rows, final int cols, final int length,
                                                final int border) {
        this.nodes  = new Node[rows][cols];
        this.rows   = rows;
        this.cols   = cols;
        this.length = length;
        this.border = border;
    }

    /**
     * Initializes this Grid.
     */
    public void init() {
        size = calcSize(length);
        createNodes();
        linkNeighboringNodes();
    }

    /*
     * Creates all Nodes in Grid.
     */
    private void createNodes() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                nodes[row][col] = createNodeAt(row, col);
            }
        }
    }

    /*
     * Calculates and links neighboring Cells for each Node.
     */
    private void linkNeighboringNodes() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                setAllLevelNeighborsFor(getNodeAt(row, col));
            }
        }
    }

    /*
     * Sets all neighboring Nodes for the specified Node.
     * @param node    to set neighbors for
     */
    private void setAllLevelNeighborsFor(final Node node) {
        node.setNeighborsForLevel(0, new Node[0]);
        for (int lvl = 1; lvl <= NEIGHBOR_LEVELS; lvl++) {
            node.setNeighborsForLevel(lvl, calcNeighborsForLevel(lvl, node));
        }
    }

    // ---------------------------------------- ABSTRACTS ------------------------------------------

    /**
     * Calculates and returns the size of Grid.
     * @return Dimension
     */
    protected abstract Dimension calcSize(int length);

    /**
     * Creates and returns a new Node at the specified row and column.
     * @param row    that Node is in
     * @param col    that Node is in
     * @return new Node
     */
    protected abstract Node createNodeAt(int row, int col);

    /**
     * Calculates and returns all the neighboring Cells.
     * @param lvl     level of neighbors to calculate
     * @param node    used to calculate neighboring Cells
     * @return neighboring Cells
     */
    protected abstract Node[] calcNeighborsForLevel(final int lvl,
                                                    final Node node);

    // ----------------------------------------- GETTERS -------------------------------------------

    /**
     * Returns true if row & column is in bounds.
     * @param row    of Grid
     * @param col    of Grid
     * @return true if in bounds
     */
    public boolean inBounds(final int row, final int col) {
        return 0 <= row && row < rows
            && 0 <= col && col < cols;
    }

    /**
     * Returns the length of a single Node.
     * @return length of Node
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns the number of rows in this Grid.
     * @return number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of columns in this Grid.
     * @return number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Returns the size of border for each Node.
     * @return border size of each Node
     */
    public int getBorder() {
        return border;
    }

    /**
     * Returns the Node at the specified row and column, if exists.
     *
     * <p><b>Assumes</b> valid row and col, or program crashes.</p>
     *
     * @param row    in Grid
     * @param col    in Grid
     * @return Node
     */
    public Node getNodeAt(final int row, final int col) {
        return nodes[row][col];
    }

    /**
     * Returns the size of Grid.
     * @return Dimension
     */
    public Dimension getSize() {
        return size;
    }
}

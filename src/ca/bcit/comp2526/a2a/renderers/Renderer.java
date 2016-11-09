package ca.bcit.comp2526.a2a.renderers;

import ca.bcit.comp2526.a2a.World;
import ca.bcit.comp2526.a2a.grids.Grid;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Renderer.
 *
 * @author  Wei Zhou
 * @version 2016-11-08
 * @since   2016-11-06
 */
public abstract class Renderer extends JPanel {

    private final JFrame     frame;
    private final World      world;
    private final Grid       grid;

    /**
     * Constructs a new Renderer.
     * @param frame    window to render GUI
     * @param world    to render
     * @param grid     structure for World
     */
    public Renderer(final JFrame frame, final World world, final Grid grid) {
        this.frame = frame;
        this.world = world;
        this.grid  = grid;
    }

    /**
     * Adds this Renderer to the frame window.
     */
    public void init() {
        setPreferredSize(grid.getSize());
        frame.add(this);
    }

    public World getWorld() {
        return world;
    }

    public Grid getGrid() {
        return grid;
    }

    /**
     * Draws the World onto frame.
     * @param graphics    object
     */
    public void paint(final Graphics graphics) {
        final Graphics2D g2 = (Graphics2D) graphics;
        setGraphicsOptions(g2);
        drawGrid(g2);
        drawLifeforms(g2);
    }

    /**
     * Draws Grid.
     * @param g2    Graphics2D object to draw on
     */
    public abstract void drawGrid(Graphics2D g2);

    /**
     * Draws Lifeforms.
     * @param g2    Graphics2D object to draw on
     */
    public abstract void drawLifeforms(Graphics2D g2);

    /*
     * Sets up anti-aliasing for drawing.
     */
    private void setGraphicsOptions(final Graphics2D g2) {
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setRenderingHints(rh);
    }
}

package ca.bcit.comp2526.a2b.renderers;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Grid;
import ca.bcit.comp2526.a2b.lifeforms.Lifeform;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import javax.swing.JPanel;

/**
 * Renderer.
 *
 * @author  Wei Zhou
 * @version 2016-11-19
 * @since   2016-11-06
 */
public abstract class Renderer extends JPanel {

    private static final Toolkit TOOLKIT;

    static {
        TOOLKIT = Toolkit.getDefaultToolkit();
    }

    private final World world;

    /**
     * Constructor.
     * @param world    to render
     */
    public Renderer(final World world) {
        this.world = world;
    }

    /**
     * Adds this Renderer to the frame window.
     */
    public void init() {
        setPreferredSize(world.getGrid().getSize());
        world.getFrame().add(this);
        world.getFrame().setLocation(centerOnScreen(world.getGrid().getSize()));
    }

    // ------------------------------------------- DRAW --------------------------------------------

    /**
     * Updates GUI.
     */
    public void update() {
        repaint();
    }

    /**
     * Draws the World onto frame.
     * @param graphics    object
     */
    public void paint(final Graphics graphics) {
        final Graphics2D g2 = (Graphics2D) graphics;
        setGraphicsOptions(g2);
        drawWorld(g2, world.getGrid(), world.getLifeforms());
    }

    // ------------------------------------------ ABSTRACTS ----------------------------------------

    /**
     * Draws Lifeforms.
     * @param g2           Graphics2D object to draw on
     * @param grid         structure
     * @param lifeforms    to draw
     */
    public abstract void drawWorld(Graphics2D g2, Grid grid, Lifeform[] lifeforms);

    // ----------------------------------------- OPTIONS -------------------------------------------

    /**
     * Returns the centre point of the screen.
     * @param size    a Dimension
     * @return a Point that refers to the centre point of the screen.
     */
    public Point centerOnScreen(final Dimension size) {
        final Dimension screenSize;
        if (size == null) {
            throw new IllegalArgumentException("Size cannot be null");
        }
        screenSize = TOOLKIT.getScreenSize();
        return (new Point((screenSize.width - size.width) / 2,
                (screenSize.height - size.height) / 2));
    }

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

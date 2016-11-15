package ca.bcit.comp2526.a2b.renderers;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Grid;
import ca.bcit.comp2526.a2b.lifeforms.Lifeform;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Renderer.
 *
 * @author  Wei Zhou
 * @version 2016-11-10
 * @since   2016-11-06
 */
public abstract class Renderer extends JPanel {

    private static final Toolkit TOOLKIT;

    static {
        TOOLKIT = Toolkit.getDefaultToolkit();
    }

//    public static Renderer create(final RendererType rt, final JFrame frame,
//                                                         final World  world) {
//
//    }

    private final JFrame frame;
    private final World  world;

    /**
     * Constructor.
     * @param frame    window to render GUI
     * @param world    to render
     */
    public Renderer(final JFrame frame, final World world) {
        this.frame = frame;
        this.world = world;
    }

    /**
     * Adds this Renderer to the frame window.
     */
    public void init() {
        setPreferredSize(world.getGrid().getSize());
        frame.add(this);
        frame.setLocation(centerOnScreen(world.getGrid().getSize()));
    }

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

    /**
     * Draws Lifeforms.
     * @param g2           Graphics2D object to draw on
     * @param grid         structure
     * @param lifeforms    to draw
     */
    public abstract void drawWorld(Graphics2D g2, Grid grid, Lifeform[] lifeforms);

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

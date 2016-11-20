package ca.bcit.comp2526.a2b.renderers;

import ca.bcit.comp2526.a2b.NotifyWhenGameOver;
import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Grid;
import ca.bcit.comp2526.a2b.lifeforms.Lifeform;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Renderer.
 *
 * @author  Wei Zhou
 * @version 2016-11-19
 * @since   2016-11-06
 */
public abstract class Renderer extends JPanel implements NotifyWhenGameOver {

    private final World   world;
    private       boolean gameover;

    /**
     * Constructor.
     * @param world    to render
     */
    public Renderer(final World world) {
        this.world = world;
        gameover   = false;
    }

    /**
     * Adds this Renderer to the frame window.
     */
    public void init() {
        setPreferredSize(world.getGrid().getSize());
        world.notifyWhenGameOver(this);
    }

    // ------------------------------------------- DRAW --------------------------------------------

    /**
     * Updates GUI.
     */
    public void update() {
        repaint();
    }

    public void gameover() {
        gameover = true;
        update();
    }

    /**
     * Draws the World onto frame.
     * @param graphics    object
     */
    public void paint(final Graphics graphics) {
        final Graphics2D g2 = (Graphics2D) graphics;

        if (gameover) {
            drawWorldMap(g2);
        } else {
            setGraphicsOptions(g2);
            drawWorld(g2, world.getGrid(), world.getLifeforms());
        }

    }

    /**
     * Draws World Map.
     * @param g2    Graphics2D object
     */
    public void drawWorldMap(final Graphics2D g2) {
        try {
            BufferedImage img = ImageIO.read(new File("worldmap.jpg"));

            final int width  = (int) world.getGrid().getSize().getWidth();
            final int height = (int) world.getGrid().getSize().getHeight();

            g2.drawImage(img, 0, 0, width, height, null);
        } catch (final IOException ex) {
            System.err.println("Renderer: can not find worldmap.jpg");
            System.err.println("God damn it, failed at the last step!");
            System.exit(1);
        }
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

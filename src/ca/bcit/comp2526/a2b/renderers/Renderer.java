package ca.bcit.comp2526.a2b.renderers;

import ca.bcit.comp2526.a2b.NotifyWhenGameOver;
import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Grid;
import ca.bcit.comp2526.a2b.lifeforms.Lifeform;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;

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
 * @version 2016-11-23
 * @since   2016-11-06
 */
public abstract class Renderer extends JPanel implements NotifyWhenGameOver {

    private static final String WORLDMAP_FILENAME = "worldmap.jpg";

    private final World   world;
    private final Grid    grid;
    private       boolean gameover;

    /**
     * Constructor.
     * @param world    to render
     */
    public Renderer(final World world) {
        this.world = world;
        grid       = world.getGrid();
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

    /**
     * Draws the World onto frame.
     * @param graphics    object
     */
    @Override
    public void paint(final Graphics graphics) {
        final Graphics2D g2 = (Graphics2D) graphics;
        setGraphicsOptions(g2);

        if (gameover) {
            drawWorldMap(g2);
            return;
        }

        drawWorld(g2, grid, world.getLifeforms());
    }

    /*
     * Draws Terrain and Lifeforms onto screen.
     * @param g2           Graphics2D
     * @param grid         containing Terrain
     * @param lifeforms    to be drawn
     */
    private void drawWorld(final Graphics2D g2, final Grid grid, final Lifeform[] lifeforms) {

        // draw Terrain
        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                drawBackgroundShape(g2, grid.getNodeAt(row, col));
            }
        }

        // draw Lifeforms
        for (Lifeform lf : lifeforms) {

            // may have null lifeforms since dead lifeforms
            // could be removed after we receive the lifeforms[]
            if (lf == null) {
                continue;
            }

            if (lf.getLifeformType() == LifeformType.PLANT) {
                drawBackgroundShape(g2, lf);
            } else {
                drawForegroundShape(g2, lf);
            }
        }
    }

    /*
     * Draws World Map.
     * @param g2    Graphics2D
     */
    private void drawWorldMap(final Graphics2D g2) {
        try {
            BufferedImage img = ImageIO.read(new File(WORLDMAP_FILENAME));

            final int width  = (int) grid.getSize().getWidth();
            final int height = (int) grid.getSize().getHeight();

            g2.drawImage(img, 0, 0, width, height, null);
        } catch (final IOException ex) {
            System.err.println("Renderer: can not find " + WORLDMAP_FILENAME);
            System.err.println("God damn it, who deleted my " + WORLDMAP_FILENAME + "!?!?");
        }
    }

    // ------------------------------------------ NOTIFIERS ----------------------------------------

    /**
     * Game is over. Called by World to signal game over.
     */
    public void gameOver() {
        gameover = true;
        update();
    }

    // ------------------------------------------ ABSTRACTS ----------------------------------------

    /**
     * Draws full-sized shape onto screen.
     * @param g2     Graphics2D
     * @param obj    to be drawn
     */
    public abstract void drawBackgroundShape(final Graphics2D g2, final Renderable obj);

    /**
     * Draws mini-sized shape onto screen.
     * @param g2     Graphics2D
     * @param obj    to be drawn
     */
    public abstract void drawMiniBackgroundShape(final Graphics2D g2, final Renderable obj);

    /**
     * Draws mini-sized other shape onto screen.
     * @param g2     Graphics2D
     * @param obj    to be drawn
     */
    public abstract void drawForegroundShape(final Graphics2D g2, final Renderable obj);

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

package ca.bcit.comp2526.a2a;

import ca.bcit.comp2526.a2a.grids.Grid;
import ca.bcit.comp2526.a2a.grids.SquareGrid;
import ca.bcit.comp2526.a2a.spawns.NaturalSpawn;
import ca.bcit.comp2526.a2a.spawns.Spawn;
import ca.bcit.comp2526.a2a.renderers.Renderer;
import ca.bcit.comp2526.a2a.renderers.SquareRenderer;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 * Main.
 *
 * @author  Wei Zhou
 * @version 2016-11-08
 * @since   2016-11-06
 */
public final class Main extends JFrame {

    private static final String  TITLE;
    private static final Toolkit TOOLKIT;

    static {
        TITLE   = "Game of Life";
        TOOLKIT = Toolkit.getDefaultToolkit();
    }

    /*
     * Constructs a new Main.
     */
    private Main() {
        final Spawn spawn;
        final Grid     grid;
        final World    world;
        final Renderer renderer;

        spawn    = new NaturalSpawn();
        grid     = new SquareGrid(5, 5, 100);
        world    = new World(spawn, grid);
        renderer = new SquareRenderer(this, world, grid);

        grid.init();
        world.init();
        renderer.init();

        setTitle(TITLE);
        pack();

        setLocation(centerOnScreen(grid.getSize()));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Drives the program.
     * @param argv    command line arguments
     */
    public static void main(final String[] argv) {
        new Main();
    }

    /**
     * Returns the centre point of the screen.
     * @param size    a Dimension
     * @return a Point that refers to the centre point of the screen.
     */
    public static Point centerOnScreen(final Dimension size) {
        final Dimension screenSize;
        if (size == null) {
            throw new IllegalArgumentException("Size cannot be null");
        }
        screenSize = TOOLKIT.getScreenSize();
        return (new Point((screenSize.width - size.width) / 2,
                (screenSize.height - size.height) / 2));
    }
}

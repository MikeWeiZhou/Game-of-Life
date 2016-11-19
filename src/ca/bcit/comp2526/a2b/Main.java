package ca.bcit.comp2526.a2b;

import ca.bcit.comp2526.a2b.grids.GridType;
import ca.bcit.comp2526.a2b.spawns.SpawnType;

import javax.swing.JFrame;

/**
 * Main.
 *
 * @author  Wei Zhou
 * @version 2016-11-19
 * @since   2016-11-06
 */
public final class Main extends JFrame {

    private static final String TITLE = "Game of Life";

    /*
     * Constructs a new Main.
     */
    private Main() {
        final World world = new World(this, GridType.SQUARE, SpawnType.NATURAL_SPAWN);
        world.init();

        setTitle(TITLE);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        addMouseListener(new TurnListener(world));
    }

    /**
     * Drives the program.
     * @param argv    command line arguments
     */
    public static void main(final String[] argv) {
        new Main();
    }
}

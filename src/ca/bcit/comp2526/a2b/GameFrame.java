package ca.bcit.comp2526.a2b;

import ca.bcit.comp2526.a2b.grids.GridType;
import ca.bcit.comp2526.a2b.spawns.SpawnType;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 * GameFrame.
 *
 * @author  Wei Zhou
 * @version 2016-11-19
 * @since   2016-11-06
 */
public final class GameFrame extends JFrame {

    private static final Toolkit TOOLKIT;
    private static final String  TITLE;

    static {
        TOOLKIT = Toolkit.getDefaultToolkit();
        TITLE   = "Game of Life";
    }

    /**
     * Drives the program.
     * @param argv    command line arguments
     */
    public static void main(final String[] argv) {
        new GameFrame();
    }

    private World world;

    /*
     * Constructs a new GameFrame.
     */
    private GameFrame() {
        final Controller controller = new Controller(this);

        setTitle(TITLE);
        setLayout(new BorderLayout(0, 0));

        controller.init();
        add(controller, BorderLayout.SOUTH);

        pack();

        setLocation(centerOnScreen(world.getGrid().getSize()));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Returns the currently active World.
     * @return World
     */
    public World getWorld() {
        return world;
    }

    /**
     * Loads World with specified GridType and SpawnType.
     * @param gridType     to load
     * @param spawnType    to load
     */
    public void loadWorld(final GridType gridType, final SpawnType spawnType) {
        if (this.world != null) {
            remove(this.world.getRenderer());
        }

        final World world = new World(gridType, spawnType);
        world.init();

        this.world = world;
        add(world.getRenderer(), BorderLayout.CENTER);

        revalidate();
    }

    /*
     * Returns the centre point of the screen.
     * @param size    a Dimension
     * @return a Point that refers to the centre point of the screen.
     */
    private Point centerOnScreen(final Dimension size) {
        final Dimension screenSize;
        if (size == null) {
            throw new IllegalArgumentException("Size cannot be null");
        }
        screenSize = TOOLKIT.getScreenSize();
        return (new Point((screenSize.width - size.width) / 2,
                (screenSize.height - size.height) / 2));
    }
}

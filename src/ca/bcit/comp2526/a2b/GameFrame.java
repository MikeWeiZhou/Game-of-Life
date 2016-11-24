package ca.bcit.comp2526.a2b;

import ca.bcit.comp2526.a2b.grids.GridType;
import ca.bcit.comp2526.a2b.renderers.Renderer;
import ca.bcit.comp2526.a2b.spawns.SpawnType;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * GameFrame.
 *
 * @author  Wei Zhou
 * @version 2016-11-23
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
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
     * Returns the current Renderer for the World.
     * @return Renderer
     */
    public Renderer getRenderer() {
        return getWorld().getRenderer();
    }

    /**
     * Loads World with specified GridType and SpawnType.
     * @param gridType     to load
     * @param spawnType    to load
     */
    public void loadWorld(final GridType gridType, final SpawnType spawnType) {
        if (gridType == null) {
            throw new IllegalStateException("GameFrame: failed to load World with a null GridType");
        }
        if (spawnType == null) {
            throw new IllegalStateException("GameFrame: failed to load World with a null "
                    + "SpawnType");
        }

        final World newWorld;

        if (world != null) {
            remove(this.world.getRenderer());
        }

        newWorld = new World(gridType, spawnType);
        newWorld.init();
        add(newWorld.getRenderer(), BorderLayout.CENTER);

        world = newWorld;
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

package ca.bcit.comp2526.a2b;

import ca.bcit.comp2526.a2b.grids.GridType;
import ca.bcit.comp2526.a2b.spawns.SpawnType;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Controller.
 *
 * @author  Wei Zhou
 * @version 2016-11-23
 * @since   2016-11-19
 */
public class Controller extends JPanel implements NotifyWhenGameOver {

    private static final boolean   DEFAULT_AUTO_TURN_TAKING;
    private static final SpawnType DEFAULT_MAP_TYPE;
    private static final GridType  DEFAULT_GRID_TYPE;

    // frame rate
    private static final TimeUnit TIME_UNIT;
    private static final int      INIT_DELAY;
    private static final int      PERIOD;

    // show avg time taken per turn calculations
    private static final boolean  SHOW_AVG_TIME_TAKEN;
    private static final int      AVG_TIME_PER_TURNS;

    static {
        DEFAULT_AUTO_TURN_TAKING = true;
        DEFAULT_MAP_TYPE  = SpawnType.GIANT_MESS;
        DEFAULT_GRID_TYPE = GridType.SQUARE;

        // frame rate
        TIME_UNIT  = TimeUnit.MILLISECONDS;
        INIT_DELAY = 1500;
        PERIOD     = 50;

        // show avg time taken per turn calculations
        SHOW_AVG_TIME_TAKEN = false;
        AVG_TIME_PER_TURNS  = 250;
    }

    private final GameFrame                gameFrame;
    private       boolean                  gameRunning;

    private       SpawnType                mapType;
    private       GridType                 gridType;

    // Buttons
    private final List<JButton>            mapButtons;
    private final List<JButton>            gridButtons;

    // Turn taking
    private       boolean                  automateTurns;
    private final TakeTurnListener         takeTurnListener;
    private final ScheduledExecutorService scheduler;
    private       ScheduledFuture<?>       scheduledTask;

    // Average time per turn counter
    private final List<Long>               timer;
    private       int                      turnsTimed;

    /**
     * Constructor.
     * @param frame    GameFrame
     */
    public Controller(final GameFrame frame) {
        gameFrame        = frame;
        gameRunning      = false;

        mapType          = DEFAULT_MAP_TYPE;
        gridType         = DEFAULT_GRID_TYPE;

        // Buttons
        mapButtons       = new ArrayList<JButton>();
        gridButtons      = new ArrayList<JButton>();

        // Turn taking
        automateTurns    = DEFAULT_AUTO_TURN_TAKING;
        takeTurnListener = new TakeTurnListener();
        scheduler        = Executors.newSingleThreadScheduledExecutor();

        // Average time per turn counter
        timer            = new ArrayList<Long>();
        turnsTimed       = 0;
    }

    /**
     * Initializes Controller.
     */
    public void init() {
        if (gameFrame == null) {
            throw new IllegalStateException("Controller: can not have null GameFrame");
        }

        addMapButtons();
        addBlankSpace();
        addGridButtons();
        addBlankSpace();
        addBlankSpace();
        addAutomateTurnButton();
        addReloadButton();

        loadGame();
    }

    /*
     * Loads game with specified map and grid type.
     */
    private void loadGame() {
        if (gameRunning) {
            gameOver();
        }
        gameRunning = true;

        gameFrame.loadWorld(gridType, mapType);
        gameFrame.getWorld().notifyWhenGameOver(this);

        loadTakeTurnListener(INIT_DELAY);
    }

    /*
     * Loads the auto/manual take turn functionality.
     */
    private void loadTakeTurnListener() {
        loadTakeTurnListener(0);
    }

    /*
     * Loads the auto/manual take turn functionality with initial delay.
     * @param initDelay    Initial delay
     */
    private void loadTakeTurnListener(final int initDelay) {
        if (!automateTurns) {
            gameFrame.getRenderer().addMouseListener(takeTurnListener);
            return;
        }
        scheduledTask = scheduler.scheduleAtFixedRate(
                takeTurnListener, initDelay, PERIOD, TIME_UNIT
        );
    }

    /*
     * Clears auto/manual take turn listener.
     */
    private void clearTakeTurnListener() {
        if (!automateTurns) {
            gameFrame.getRenderer().removeMouseListener(takeTurnListener);
            return;
        }
        scheduledTask.cancel(true);
    }

    /*
     * Toggles auto/manual take turn functionality.
     */
    private void toggleTakeTurnAutomation() {
        clearTakeTurnListener();
        automateTurns = !automateTurns;
        loadTakeTurnListener();
    }

    /*
     * Take a turn.
     */
    private void takeTurn() {
        long t1 = 0;
        long t2;

        if (SHOW_AVG_TIME_TAKEN) {
            if (++turnsTimed < AVG_TIME_PER_TURNS) {
                t1 = new Date().getTime();
            }
        }

        gameFrame.getWorld().takeTurn();

        if (SHOW_AVG_TIME_TAKEN) {
            if (turnsTimed < AVG_TIME_PER_TURNS) {
                t2 = new Date().getTime();
                long avg = t2 - t1;
                timer.add(avg);
            } else if (turnsTimed == AVG_TIME_PER_TURNS) {
                long sum = 0;
                for (long time : timer) {
                    sum += time;
                }
                long avg = sum / timer.size();
                System.out.println("Average time per turn calculations: " + avg + " ms");
                turnsTimed = 0;
                timer.clear();
            }
        }
    }

    // ------------------------------------------ BUTTONS ------------------------------------------

    /*
     * Adds map reload button.
     */
    private void addReloadButton() {
        final JButton button = new JButton("Reload");
        button.addActionListener(loadGameListener);
        add(button);
    }

    /*
     * Adds automate turn button.
     */
    private void addAutomateTurnButton() {
        final String  label  = automateTurns ? "Manual Turn Taking" : "Auto Turn Taking";
        final JButton button = new JButton(label);
        button.addActionListener(toggleTakeTurnAutomationListener);
        add(button);
    }

    /*
     * Adds blank space.
     */
    private void addBlankSpace() {
        final int blocksize = 50;
        final int height    = 1;
        add(Box.createRigidArea(new Dimension(blocksize, height)));
    }

    /*
     * Adds map buttons.
     */
    private void addMapButtons() {
        add(new JLabel("Map:"));
        for (SpawnType map : SpawnType.values()) {
            final JButton button = new JButton(map.getName());
            mapButtons.add(button);
            button.addActionListener(setMapType);
            button.putClientProperty("type", map);
            add(button);

            if (mapType.equals(map)) {
                button.setEnabled(false);
            }
        }
    }

    /*
     * Adds grid buttons.
     */
    private void addGridButtons() {
        add(new JLabel("Grid:"));
        for (GridType type : GridType.values()) {
            final JButton button = new JButton(type.getName());
            gridButtons.add(button);
            button.addActionListener(setGridType);
            button.putClientProperty("type", type);
            add(button);

            if (gridType.equals(type)) {
                button.setEnabled(false);
            }
        }
    }

    // ---------------------------------------- NOTIFICATIONS --------------------------------------

    /**
     * Stops the game. Called by World when game is over.
     */
    @Override
    public void gameOver() {
        clearTakeTurnListener();
        gameRunning = false;
    }

    // -------------------------------------- INNER CLASSES ----------------------------------------

    /*
     * Schedulable take turn listener.
     */
    private class TakeTurnListener extends MouseAdapter implements Runnable {
        @Override
        public void run() {
            takeTurn();
        }

        @Override
        public void mouseClicked(final MouseEvent event) {
            takeTurn();
        }
    }

    /*
     * ActionListener for toggling take turn automation.
     */
    private ActionListener toggleTakeTurnAutomationListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            final String label;

            toggleTakeTurnAutomation();
            label = automateTurns ? "Manual Turn Taking" : "Auto Turn Taking";
            ((JButton) (event.getSource())).setText(label);
        }
    };

    /*
     * ActionListener for loading game.
     */
    private ActionListener loadGameListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            loadGame();
        }
    };

    /*
     * ActionListener for setting mapType.
     */
    private final ActionListener setMapType = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            final JButton source = (JButton) event.getSource();
            for (JButton button : mapButtons) {
                if (source.equals(button)) {
                    button.setEnabled(false);
                    mapType = (SpawnType) button.getClientProperty("type");
                } else {
                    button.setEnabled(true);
                }
            }
            loadGame();
        }
    };

    /*
     * ActionListener for setting gridType.
     */
    private final ActionListener setGridType = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            final JButton source = (JButton) event.getSource();
            for (JButton button : gridButtons) {
                if (source.equals(button)) {
                    button.setEnabled(false);
                    gridType = (GridType) button.getClientProperty("type");
                } else {
                    button.setEnabled(true);
                }
            }
            loadGame();
        }
    };
}

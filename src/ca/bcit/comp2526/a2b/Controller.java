package ca.bcit.comp2526.a2b;

import ca.bcit.comp2526.a2b.grids.GridType;
import ca.bcit.comp2526.a2b.spawns.SpawnType;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
 * @version 2016-11-20
 * @since   2016-11-19
 */
public class Controller extends JPanel implements NotifyWhenGameOver {

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
    private final ScheduledExecutorService scheduler;
    private       ScheduledFuture<?>       futureTask;
    private       boolean                  gameRunning;

    private       SpawnType                mapType;
    private       GridType                 gridType;

    private final List<JButton>            mapButtons;
    private final List<JButton>            gridButtons;

    private final List<Long>               timer;
    private       int                      turnsTimed;

    /**
     * Constructor.
     * @param frame    GameFrame
     */
    public Controller(final GameFrame frame) {
        gameFrame   = frame;
        scheduler   = Executors.newSingleThreadScheduledExecutor();
        gameRunning = false;

        mapButtons  = new ArrayList<JButton>();
        gridButtons = new ArrayList<JButton>();

        mapType     = DEFAULT_MAP_TYPE;
        gridType    = DEFAULT_GRID_TYPE;

        timer       = new ArrayList<Long>();
        turnsTimed  = 0;
    }

    /**
     * Initializes Controller.
     */
    public void init() {
        if (gameFrame == null) {
            throw new IllegalStateException("Controller: can not have null GameFrame");
        }

        add(new JLabel("Map:"));
        initMapButtons();

        add(Box.createRigidArea(new Dimension(50, 1)));

        add(new JLabel("Grid:"));
        initGridButtons();

        add(Box.createRigidArea(new Dimension(100, 1)));

        final JButton reloadButton = new JButton("Reload");
        reloadButton.addActionListener(loadGameListener);
        add(reloadButton);

        loadgame();
    }

    /*
     * Initialize map buttons.
     */
    private void initMapButtons() {
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
     * Initialize grid buttons.
     */
    private void initGridButtons() {
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

    /*
     * Loads game with specified map and grid type.
     */
    private void loadgame() {
        if (gameRunning) {
            gameover();
        }
        gameRunning = true;

        gameFrame.loadWorld(gridType, mapType);
        gameFrame.getWorld().notifyWhenGameOver(this);

        futureTask = scheduler.scheduleAtFixedRate(new TakeTurn(), INIT_DELAY, PERIOD, TIME_UNIT);
    }

    /**
     * Take a turn.
     */
    protected class TakeTurn implements Runnable {

        /**
         * Take a turn.
         */
        @Override
        public void run() {
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
    }

    // ------------------------------------- NOTIFICATIONS --------------------------------------

    /**
     * Stops the game. Called by World when game is over.
     */
    @Override
    public void gameover() {
        futureTask.cancel(true);
        gameRunning = false;
    }

    // ------------------------------------- ACTION LISTENERS --------------------------------------

    /*
     * ActionListener for loading game.
     */
    private final ActionListener loadGameListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            loadgame();
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
            loadgame();
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
            loadgame();
        }
    };
}

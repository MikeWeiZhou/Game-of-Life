package ca.bcit.comp2526.a2b;

import ca.bcit.comp2526.a2b.grids.GridType;
import ca.bcit.comp2526.a2b.spawns.SpawnType;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
 * @version 2016-11-19
 * @since   2016-11-19
 */
public class Controller extends JPanel implements NotifyWhenGameOver {

    private static final SpawnType DEFAULT_MAP_TYPE;
    private static final GridType  DEFAULT_GRID_TYPE;

    private static final TimeUnit TIME_UNIT;
    private static final int      INIT_DELAY;
    private static final int      PERIOD;

    static {
        DEFAULT_MAP_TYPE  = SpawnType.NATURAL_SPAWN;
        DEFAULT_GRID_TYPE = GridType.SQUARE;

        TIME_UNIT  = TimeUnit.MILLISECONDS;
        INIT_DELAY = 1500;
        PERIOD     = 50;
    }

    private final GameFrame                gameFrame;
    private       ScheduledExecutorService scheduler;
    private       ScheduledFuture<?>       futureTask;
    private       boolean                  gameRunning;

    private SpawnType mapType;
    private GridType  gridType;

    private List<JButton> mapButtons;
    private List<JButton> gridButtons;

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

        mapType  = DEFAULT_MAP_TYPE;
        gridType = DEFAULT_GRID_TYPE;
    }

    /**
     * Initializes Controller.
     */
    public void init() {
        if (gameFrame == null) {
            System.exit(1);
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

        final Runnable r = new Runnable() {
            @Override
            public void run() {
                gameFrame.getWorld().takeTurn();
            }
        };

        futureTask = scheduler.scheduleAtFixedRate(r, INIT_DELAY, PERIOD, TIME_UNIT);
        gameFrame.getWorld().notifyWhenGameOver(this);
    }

    // ------------------------------------- NOTIFICATIONS --------------------------------------

    /**
     * Stops the game. Called by outsider when game is over.
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
    private ActionListener loadGameListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            loadgame();
        }
    };

    /*
     * ActionListener for setting mapType.
     */
    private ActionListener setMapType = new ActionListener() {
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
    private ActionListener setGridType = new ActionListener() {
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

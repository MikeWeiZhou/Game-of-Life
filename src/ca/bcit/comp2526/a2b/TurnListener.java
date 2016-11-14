package ca.bcit.comp2526.a2b;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * TurnListener.
 *
 * @author   Wei Zhou
 * @version 2016-11-13
 * @since   2016-11-10
 */
public class TurnListener extends MouseAdapter{

    private static final TimeUnit TIME_UNIT;
    private static final int      INIT_DELAY;
    private static final int      PERIOD;

    static {
        TIME_UNIT  = TimeUnit.MILLISECONDS;
        INIT_DELAY = 0;
        PERIOD     = 150;
    }

    private final World   world;
    private       boolean engaged;

    public TurnListener(final World world) {
        this.world   = world;
        this.engaged = false;
    }

    /**
     * Takes a turn.
     * @param ev    MouseEvent
     */
    @Override
    public void mouseClicked(final MouseEvent ev) {
        if (engaged) {
            return;
        }
        engaged = true;

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                world.takeTurn();
            }
        };

        scheduler.scheduleAtFixedRate(r, INIT_DELAY, PERIOD, TIME_UNIT);
    }
}

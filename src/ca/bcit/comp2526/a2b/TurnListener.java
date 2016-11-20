package ca.bcit.comp2526.a2b;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class TurnListener extends MouseAdapter implements NotifyWhenGameOver {

    private static final TimeUnit TIME_UNIT;
    private static final int      INIT_DELAY;
    private static final int      PERIOD;

    private static final int      AVG_TIME_PER_TURNS;

    static {
        TIME_UNIT  = TimeUnit.MILLISECONDS;
        INIT_DELAY = 0;
        PERIOD     = 50;

        AVG_TIME_PER_TURNS = 250;
    }

    private final ScheduledExecutorService scheduler;
    private final World                    world;
    private       boolean                  engaged;

    private final List<Long> timer;
    private       int        turnsTimed;

    /**
     * Constructor.
     * @param world    that this Listener adheres to
     */
    public TurnListener(final World world) {
        scheduler    = Executors.newScheduledThreadPool(1);
        this.world   = world;
        this.engaged = false;

        timer      = new ArrayList<Long>();
        turnsTimed = 0;

        world.notifyWhenGameOver(this);
    }

    /**
     * Game is over.
     */
    public void gameover() {
        scheduler.shutdown();
        System.out.println("Shutting down scheduler...");
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

        final Runnable r = new Runnable() {
            @Override
            public void run() {
                long t1 = 0;
                long t2;
                if (++turnsTimed < AVG_TIME_PER_TURNS) {
                    t1 = new Date().getTime();
                }

                world.takeTurn();

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
        };

        scheduler.scheduleAtFixedRate(r, INIT_DELAY, PERIOD, TIME_UNIT);
    }
}

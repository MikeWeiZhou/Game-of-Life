package ca.bcit.comp2526.a2b.renderers;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Grid;
import ca.bcit.comp2526.a2b.lifeforms.Lifeform;

import javax.swing.JFrame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * SquareRenderer.
 *
 * @author  Wei Zhou
 * @version 2016-11-08
 * @since   2016-11-06
 */
public class SquareRenderer extends Renderer {

    /**
     * Constructs a new SquareRenderer.
     * @param frame    window to render GUI
     * @param world    to render
     * @param grid     structure for World
     */
    public SquareRenderer(final JFrame frame, final World world, final Grid grid) {
        super(frame, world, grid);
    }

    /**
     * Does nothing; Lifeforms are squares!
     * @param g2    Graphics2D object to draw on
     */
    public void drawGrid(final Graphics2D g2) {
    }

    /**
     * Draws Lifeforms.
     * @param g2    Graphics2D object to draw on
     */
    public void drawLifeforms(Graphics2D g2) {
        final int border    = getGrid().getBorder();
        final int length    = getGrid().getLength();
        final Lifeform[] lf = getWorld().getLifeforms();

        for (int i = 0; i < lf.length; i++) {
            Point p = lf[i].getNode().getPoint();

            g2.setPaint(lf[i].getColor());
            g2.fill(new Rectangle2D.Double(p.x, p.y, length, length));
        }
    }
}

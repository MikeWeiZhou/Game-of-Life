package ca.bcit.comp2526.a2b.renderers;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Grid;
import ca.bcit.comp2526.a2b.grids.Node;
import ca.bcit.comp2526.a2b.lifeforms.Lifeform;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;

import javax.swing.JFrame;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * SquareRenderer.
 *
 * @author  Wei Zhou
 * @version 2016-11-13
 * @since   2016-11-06
 */
public class SquareRenderer extends Renderer {

    // size of plants (0.1 - 1.0)
    private static final float PLANT_SIZE  = 1f;

    // size of others (0.1 - 1.0)
    private static final float OTHER_SIZE = 0.8f;

    /**
     * Constructs a new SquareRenderer.
     * @param frame    window to render GUI
     * @param world    to render
     */
    public SquareRenderer(final JFrame frame, final World world) {
        super(frame, world);
    }

    /**
     * Draws the World.
     * @param g2           Graphics2D object to draw on
     * @param grid         structure
     * @param lifeforms    to draw
     */
    public void drawWorld(final Graphics2D g2, final Grid grid,
                                               final Lifeform[] lifeforms) {

        final double length      = grid.getLength();
        final double plantLen    = (length * PLANT_SIZE);
        final double plantOffset = (length - plantLen) / 2;
        final double otherLen    = (length * OTHER_SIZE);
        final double otherOffset = (length - otherLen) / 2;

        // draw Terrains
        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                Node n = grid.getNodeAt(row, col);
                g2.setPaint(n.getColor());
                g2.fill(new Rectangle2D.Double(
                        n.getPoint().x,
                        n.getPoint().y,
                        length,
                        length
                ));
            }
        }

        // draw Lifeforms
        for (int i = 0; i < lifeforms.length; i++) {
            Node n = lifeforms[i].getNode();
            g2.setPaint(lifeforms[i].getColor());

            if (lifeforms[i].getLifeformType() == LifeformType.PLANT) {
                g2.fill(new Rectangle2D.Double(
                        n.getPoint().x + plantOffset,
                        n.getPoint().y + plantOffset,
                        plantLen,
                        plantLen
                ));
            } else {
                g2.fill(new Ellipse2D.Double(
                        n.getPoint().x + otherOffset,
                        n.getPoint().y + otherOffset,
                        otherLen,
                        otherLen
                ));
            }
        }
    }
}

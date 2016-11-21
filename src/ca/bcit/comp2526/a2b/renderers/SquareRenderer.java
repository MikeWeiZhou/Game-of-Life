package ca.bcit.comp2526.a2b.renderers;

import ca.bcit.comp2526.a2b.World;
import ca.bcit.comp2526.a2b.grids.Grid;
import ca.bcit.comp2526.a2b.lifeforms.Lifeform;
import ca.bcit.comp2526.a2b.lifeforms.LifeformType;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * SquareRenderer.
 *
 * @author  Wei Zhou
 * @version 2016-11-20
 * @since   2016-11-06
 */
public class SquareRenderer extends Renderer {

    /**
     * Constructs a new SquareRenderer.
     * @param world    to render
     */
    public SquareRenderer(final World world) {
        super(world);
    }

    /**
     * Draws full-sized square onto screen.
     * @param g2     Graphics2D
     * @param obj    to be drawn
     */
    @Override
    public void drawFull(final Graphics2D g2, final Renderable obj) {
        g2.setPaint(obj.getColor());
        g2.fill(newSquare(obj.getPoint().x, obj.getPoint().y, obj.getLength()));
    }

    /**
     * Draws mini-sized square onto screen.
     * @param g2     Graphics2D
     * @param obj    to be drawn
     */
    @Override
    public void drawMini(final Graphics2D g2, final Renderable obj) {
        final float ratio = 0.8f;
        final float length = obj.getLength() * ratio;
        final float offset = (obj.getLength() - length) / 2;

        g2.setPaint(obj.getColor());
        g2.fill(newSquare(obj.getPoint().x + offset, obj.getPoint().y + offset, length));
    }

    /**
     * Draws mini-sized circle onto screen.
     * @param g2     Graphics2D
     * @param obj    to be drawn
     */
    @Override
    public void drawOtherMini(final Graphics2D g2, final Renderable obj) {
        final float ratio = 0.8f;
        final float length = obj.getLength() * ratio;
        final float offset = (obj.getLength() - length) / 2;

        g2.setPaint(obj.getColor());
        g2.fill(newCircle(obj.getPoint().x + offset, obj.getPoint().y + offset, length));
    }

    private Rectangle2D newSquare(final float px, final float py, final float length) {
        return new Rectangle2D.Double(px, py, length, length);
    }

    private Ellipse2D newCircle(final float px, final float py, final float length) {
        return new Ellipse2D.Double(px, py, length, length);
    }
}

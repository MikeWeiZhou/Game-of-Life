package ca.bcit.comp2526.a2b.renderers;

import ca.bcit.comp2526.a2b.World;

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
    public void drawBackgroundShape(final Graphics2D g2, final Renderable obj) {
        g2.setPaint(obj.getColor());
        g2.fill(newSquare(obj.getPoint().x, obj.getPoint().y, obj.getSize()));
    }

    /**
     * Draws mini-sized square onto screen.
     * @param g2     Graphics2D
     * @param obj    to be drawn
     */
    @Override
    public void drawMiniBackgroundShape(final Graphics2D g2, final Renderable obj) {
        final float ratio = 0.8f;
        final float length = obj.getSize() * ratio;
        final float offset = (obj.getSize() - length) / 2;

        g2.setPaint(obj.getColor());
        g2.fill(newSquare(obj.getPoint().x + offset, obj.getPoint().y + offset, length));
    }

    /**
     * Draws mini-sized circle onto screen.
     * @param g2     Graphics2D
     * @param obj    to be drawn
     */
    @Override
    public void drawForegroundShape(final Graphics2D g2, final Renderable obj) {
        final float ratio = 0.8f;
        final float length = obj.getSize() * ratio;
        final float offset = (obj.getSize() - length) / 2;

        g2.setPaint(obj.getColor());
        g2.fill(newCircle(obj.getPoint().x + offset, obj.getPoint().y + offset, length));
    }

    /*
     * Returns a new Rectangle that is a square.
     * @param px        top left x coordinate
     * @param py        top left y coordinate
     * @param length    of each side
     * @return a Rectangle
     */
    private Rectangle2D newSquare(final float px, final float py, final float length) {
        return new Rectangle2D.Double(px, py, length, length);
    }

    /*
     * Returns a new Ellipse that is a circle.
     * @param px        top left x coordinate
     * @param py        top left x coordinate
     * @param length    length of the circle (2 x radius)
     * @return a new Ellipse
     */
    private Ellipse2D newCircle(final float px, final float py, final float length) {
        return new Ellipse2D.Double(px, py, length, length);
    }
}

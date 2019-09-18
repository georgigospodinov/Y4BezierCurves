package components.bezier;

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics2D;

import static main.Application.APP;
import static main.Main.CONFIG;

/**
 * Represents a control point for a {@link components.BezierCurve}.
 *
 * @author 150009974
 * @version 1.2
 */
public class ControlPoint extends Point {


    /** The {@link Color} of the control points. */
    private static final Color COLOR = CONFIG.getColor("control point");

    /** The radius of the control points. */
    private static final int RADIUS = CONFIG.getAnyInt("control point radius");

    /**
     * Constructs a {@link ControlPoint} with
     * the same location as the specified {@link Point}.
     *
     * @param p a {@link Point}
     */
    public ControlPoint(final Point p) {
        super(p);
    }

    /**
     * Tells whether the specified {@link Point}
     * is in this {@link ControlPoint}.
     * That happens if the distance
     * from this {@link ControlPoint} to the specified {@link Point}
     * is less then or equal to the radius of a control point.
     *
     * @param p the {@link Point} to check
     * @return true iff
     * the specified {@link Point} is inside this {@link ControlPoint}
     * @see ControlPoint#RADIUS
     */
    public boolean contains(final Point p) {
        return distance(x, y, p.x, p.y) <= RADIUS;
    }

    /**
     * Returns a {@link String} representation of this {@link ControlPoint} as
     * the two coordinates of the point, separated by a comma and a space,
     * and enclosed in parentheses.
     *
     * @return the coordinates of this {@link ControlPoint}
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Paints this {@link ControlPoint}
     * using the given {@link Graphics2D} object.
     *
     * @param g  the {@link Graphics2D} object to use
     * @param id the id to draw next to the point
     */
    public void paint(final Graphics2D g, final String id) {
        g.setColor(COLOR);
        int arcX = this.x - RADIUS;
        int arcY = this.y - RADIUS;
        int diameter = RADIUS * 2;

        if (APP.controlPointsAreVisible()) {
            g.fillOval(arcX, arcY, diameter, diameter);
        }
        if (APP.controlPointsIDsAreVisible()) {
            g.drawString(id, arcX + diameter, (float) arcY);
        }
    }

}

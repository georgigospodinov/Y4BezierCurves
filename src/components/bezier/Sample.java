package components.bezier;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;

import static main.Application.APP;
import static main.Main.CONFIG;

/**
 * Represents a sample point on a {@link components.BezierCurve}.
 * A sample may have it's tangent and curvature vector active.
 *
 * @author 150009974
 * @version 1.1
 */
public class Sample extends Point2D.Double {

    /** The {@link Color} of the tangent to a {@link Sample}. */
    private static final Color TANGENT_COLOR = CONFIG.getColor("tangent");

    /** The {@link Color} of the curvature from a {@link Sample}. */
    private static final Color CURVATURE_COLOR = CONFIG.getColor("curvature");

    /** The {@link Color} of a {@link Sample}. */
    private static final Color COLOR = CONFIG.getColor("sample point");

    /** The radius of the sample points. */
    private static final int RADIUS = CONFIG.getAnyInt("sample point radius");

    /** The id of this {@link Sample} that is drawn next to it. */
    private final String uniqueID;

    /** The tangent to this {@link Sample}. */
    private Line2D.Double tangent;

    /** The line representing the curvature vector from this {@link Sample}. */
    private Line2D.Double curvature;

    /** Tells whether the tangent is to be drawn on the screen. */
    private boolean tangentActivated = APP.tangentsAreVisible();

    /** Tells whether the curvature is to be drawn on the screen. */
    private boolean curvatureActivated = APP.curvaturesAreVisible();

    /**
     * Creates a {@link Sample} at the end of the specified segment.
     *
     * @param segment the segment to be sampled
     * @param id      the unique id of this {@link Sample}
     */
    public Sample(final CurveSegment segment, final int id) {
        super(segment.getX2(), segment.getY2());
        uniqueID = String.valueOf(id);
        initializeTangent(segment.getTangentVector());
        initializeCurvature(segment.getCurvatureVector());
    }

    /**
     * Initializes the tangent line such that
     * it passes through this {@link Sample}
     * and goes in the direction specified by the tangent vector.
     * It is assumed that the vector originates at this {@link Sample}.
     * So one end of the line is equal to the sum of
     * this {@link Sample} and the specified vector.
     *
     * @param tangentVector the direction of the line
     */
    private void initializeTangent(final Point2D.Double tangentVector) {
        // Note that the vector originates at this point, so
        // in order to draw the tangent line, we need to add the two together
        double x1 = this.x + tangentVector.x;
        double y1 = this.y + tangentVector.y;

        // This calculation finds a point(x2, y2) such that
        // the average of (x2, y2) and (x1, y1) is this Sample.
        // This way this Sample is in the middle of
        // the line from (x1, y1) to (x2, y2).
        double x2 = 2 * this.x - x1;
        double y2 = 2 * this.y - y1;
        tangent = new Line2D.Double(x1, y1, x2, y2);
    }

    /**
     * Initializes the curvature line such that
     * it passes through this {@link Sample}
     * and goes in the direction specified by the curvature vector.
     * It is assumed that the vector originates at this {@link Sample}.
     * So one end of the line is equal to the sum of
     * this {@link Sample} and the specified vector.
     *
     * @param curvatureVector the direction of the line
     */
    private void initializeCurvature(final Point2D.Double curvatureVector) {
        // Note that the vector originates at this point, so
        // in order to draw the curvature line, we need to add the two together
        double x1 = this.x + curvatureVector.x;
        double y1 = this.y + curvatureVector.y;
        curvature = new Line2D.Double(x1, y1, this.x, this.y);
    }

    /**
     * Tells whether the specified {@link Point} is within this {@link Sample}.
     * That happens if the distance
     * from this {@link Sample} to the specified {@link Point}
     * is less then or equal to the radius of a sample.
     *
     * @param clicked the {@link Point} to check
     * @return true iff
     * the specified {@link Point} is inside this {@link Sample}
     * @see Sample#RADIUS
     */
    public boolean contains(final Point clicked) {
        return distance(x, y, clicked.x, clicked.y) <= RADIUS;
    }

    /** Toggles the active state of the tangent vector. */
    public void toggleTangent() {
        tangentActivated = !tangentActivated;
    }

    /** Toggles the active state of the curvature vector. */
    public void toggleCurvature() {
        curvatureActivated = !curvatureActivated;
    }

    /**
     * Sets whether the tangent should be drawn.
     *
     * @param active whether the tangent should be drawn
     */
    public void setTangentActivated(final boolean active) {
        this.tangentActivated = active;
    }

    /**
     * Sets whether the curvature should be drawn.
     *
     * @param active whether the curvature should be drawn
     */
    public void setCurvatureActivated(final boolean active) {
        this.curvatureActivated = active;
    }

    /**
     * Returns a {@link String} representation of this {@link Sample} as
     * the two coordinates of the point, separated by a comma and a space,
     * and enclosed in parentheses.
     *
     * @return the coordinates of this {@link Sample}
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Paints this {@link Sample} using the given {@link Graphics2D} object.
     *
     * @param g the {@link Graphics2D} object to use
     */
    public void paint(final Graphics2D g) {
        g.setColor(COLOR);

        double arcX = this.x - RADIUS;
        double arcY = this.y - RADIUS;
        int diameter = RADIUS * 2;

        if (APP.samplesAreVisible()) {
            g.fill(new Ellipse2D.Double(arcX, arcY, diameter, diameter));
        }

        if (APP.sampleIDsAreVisible()) {
            g.drawString(uniqueID, (float) (arcX + diameter), (float) arcY);
        }

        if (tangentActivated) {
            g.setColor(TANGENT_COLOR);
            g.draw(tangent);
        }
        if (curvatureActivated) {
            g.setColor(CURVATURE_COLOR);
            g.draw(curvature);
        }
    }

}

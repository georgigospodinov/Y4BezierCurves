package components.bezier;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import static components.BezierCurve.CURVE;
import static main.Application.APP;
import static main.Main.CONFIG;

/**
 * Represents a segment of a Bezier Curve
 * as a straight line between two consecutive points.
 *
 * @author 150009974
 * @version 1.3
 */
public class CurveSegment extends Line2D.Double {

    /** The length of the tangents to draw. */
    private static final double TANGENT_LENGTH =
            CONFIG.getDouble("tangent length") * APP.getWidth();

    /** The length of the curvatures to draw. */
    private static final double CURVATURE_LENGTH =
            CONFIG.getDouble("curvature length") * APP.getWidth();

    /** The vector tangent from the end point of this {@link CurveSegment}. */
    private Point2D.Double tangentVector;

    /** The curvature vector from the end point of this {@link CurveSegment}. */
    private Point2D.Double curvatureVector;

    /** The proportion u, at which this tangent ends. */
    private double u;

    /**
     * Creates a {@link CurveSegment} between the given start and end points.
     *
     * @param start         the start of the segment
     * @param end           the end of the segment
     * @param proportionEnd the u value at the end of the curve
     */
    public CurveSegment(final Point2D.Double start,
                        final Point2D.Double end,
                        final double proportionEnd) {
        super(start, end);
        u = proportionEnd;
    }

    /**
     * Finds the vector that is
     * normal to the first derivative
     * and in the direction of the second derivative.
     * This happens by calculating the angle between the two and then
     * returning the appropriate normal.
     *
     * @param firstDerivative the first derivative
     * @param secondDerivative the second derivative
     * @return the proper curvature vector
     */
    private static Point2D.Double determineCurvature(
            final Point2D.Double firstDerivative,
            final Point2D.Double secondDerivative) {
        double tangentAngle;
        double curvatureAngle;
        tangentAngle = Math.atan2(firstDerivative.y, firstDerivative.x);
        curvatureAngle = Math.atan2(secondDerivative.y, secondDerivative.x);
        double theta = curvatureAngle - tangentAngle;
        if (theta < 0) {
            theta += Math.PI * 2;
        }
        if (theta <= Math.PI) {
            return new Point2D.Double(-firstDerivative.y, firstDerivative.x);
        } else {
            return new Point2D.Double(firstDerivative.y, -firstDerivative.x);
        }
    }

    /**
     * Calculates and returns the length of the segment.
     * It is the euclidean distance between the start and end points.
     *
     * @return the length of the segment
     */
    public double length() {
        return Point2D.distance(x1, y1, x2, y2);
    }

    /** @return the tangent vector from the end point of the segment */
    public Point2D.Double getTangentVector() {
        if (tangentVector == null) {
            instantiateVectors();
        }
        return new Point2D.Double(tangentVector.x, tangentVector.y);
    }

    /** @return the curvature vector from the end point of the segment */
    public Point2D.Double getCurvatureVector() {
        if (curvatureVector == null) {
            instantiateVectors();
        }
        return new Point2D.Double(curvatureVector.x, curvatureVector.y);
    }

    /** Instantiates the tangent and curvature vector. */
    public void instantiateVectors() {
        Point2D.Double firstDerivative = CURVE.calculateTangent(u);
        Point2D.Double secondDerivative;
        Point2D.Double curvature;

        double length;
        length = Point2D.distance(firstDerivative.x, firstDerivative.y, 0, 0);
        double x = TANGENT_LENGTH * firstDerivative.x / length;
        double y = TANGENT_LENGTH * firstDerivative.y / length;
        tangentVector = new Point2D.Double(x, y);

        secondDerivative = CURVE.calculateSecondDerivative(u);
        curvature = determineCurvature(firstDerivative, secondDerivative);
        length = Point2D.distance(curvature.x, curvature.y, 0, 0);
        x = CURVATURE_LENGTH * curvature.x / length;
        y = CURVATURE_LENGTH * curvature.y / length;
        curvatureVector = new Point2D.Double(x, y);
    }

}

package components;

import components.bezier.ControlPoint;
import components.bezier.CurveSegment;
import components.bezier.Sample;
import util.PrintFormatting;

import java.awt.geom.Point2D;
import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.function.Function;

import static main.Application.APP;
import static main.Main.CONFIG;

/**
 * Represents a Bezier Curve.
 *
 * @author 150009974
 * @version 3.2
 */
public final class BezierCurve {

    /** The number of points used to draw a poly line for the curve. */
    private static final int NUMBER_OF_POINTS =
            CONFIG.getAnyInt("curve number of points");

    /** The {@link Color} of the curve. */
    private static final Color CURVE_COLOR = CONFIG.getColor("curve");

    /**
     * The number of {@link ControlPoint}s, that, when reached,
     * cause the "long curve message" to appear.
     */
    private static final int LONG_CURVE_THRESHOLD =
            CONFIG.getAnyInt("long curve threshold");

    /** The curve that is displayed. */
    public static final BezierCurve CURVE = new BezierCurve();

    /** The control points forming the curve. */
    private LinkedList<ControlPoint> controlPoints = new LinkedList<>();

    /** The uniformly sampled points for tangent and curvature vectors. */
    private LinkedList<Sample> samples = new LinkedList<>();

    /** The length of the curve. */
    private double length;

    /** The segments between every two consecutive points. */
    private LinkedList<CurveSegment> curveSegments = new LinkedList<>();

    /** Hides the constructor, so that there can only be one curve. */
    private BezierCurve() {
    }

    /**
     * Processes the described click.
     * It is assumed that a click has happened with
     * either the left mouse button or the right mouse button.
     * If left clicked on a {@link ControlPoint},
     * that {@link ControlPoint} is removed from the {@link ControlPoint}s
     * of this {@link BezierCurve}.
     * If left clicked on a {@link Sample},
     * that {@link Sample}'s tangent vector is toggled.
     * Otherwise, a left click creates a new {@link ControlPoint}.
     *
     * If right clicked on a {@link Sample},
     * that {@link Sample}'s curvature vector is toggled.
     * Otherwise, a right click prints this {@link BezierCurve} to the terminal.
     *
     * @param left    whether this click was a left-click
     * @param clicked the {@link Point} that the user clicked
     * @see BezierCurve#removeControlPoint(Point)
     * @see BezierCurve#toggleTangent(Point)
     * @see BezierCurve#toggleCurvature(Point)
     */
    public void processClick(final boolean left, final Point clicked) {
        if (left) {
            if (removeControlPoint(clicked)) {
                APP.setMessage("Control Point removed.");
                update();
            } else if (!toggleTangent(clicked)) {
                controlPoints.add(new ControlPoint(clicked));
                showDescriptiveMessage();
                update();
            }
        } else {
            if (!toggleCurvature(clicked)) {
                PrintFormatting.print(this);
            }
        }
    }

    /** Shows a message specific to the number of {@link ControlPoint}s. */
    private void showDescriptiveMessage() {
        int amount = controlPoints.size();
        if (amount == 1) {
            APP.setMessage("That's it! Keep clicking!");
        } else if (amount == 2) {
            APP.setMessage("Line down, curve to go!\n"
                    + "Create one more Control Point!");
        } else if (amount >= LONG_CURVE_THRESHOLD * 3 / 2) {
            APP.setMessage("Just to let you know,\n"
                    + "you can press Space to remove all Control Points.");
        } else if (amount >= LONG_CURVE_THRESHOLD) {
            APP.setMessage("Woah! Now that's a curve!");
        } else if (amount >= LONG_CURVE_THRESHOLD / 2) {
            APP.setMessage("By the way, you can remove a Control Point.\n"
                    + "Just left click on it.");
        } else {
            APP.setMessage("Curve created!");
        }
    }

    /**
     * Removes the {@link ControlPoint} that the user clicked.
     * If the specified {@link Point} is inside a {@link ControlPoint},
     * that {@link ControlPoint} is removed
     * from the list of {@link ControlPoint}s, and returns true.
     * Otherwise, just returns false.
     *
     * @param clicked the {@link Point} that the user clicked
     * @return true iff a {@link ControlPoint} was removed
     */
    private boolean removeControlPoint(final Point clicked) {
        Iterator<ControlPoint> iterator = controlPoints.iterator();
        while (iterator.hasNext()) {
            ControlPoint cp = iterator.next();
            if (cp.contains(clicked)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Toggles the tangents at the clicked {@link Sample}.
     * If the specified {@link Point} is inside a {@link Sample},
     * that {@link Sample}'s tangent is toggled, and returns true.
     * Otherwise, just returns false.
     *
     * @param clicked the {@link Point} that the user clicked
     * @return true iff a tangent was toggled
     * @see Sample#toggleTangent()
     */
    private boolean toggleTangent(final Point clicked) {
        for (Sample sample : samples) {
            if (sample.contains(clicked)) {
                sample.toggleTangent();
                return true;
            }
        }
        return false;
    }

    /**
     * Toggles the curvature line at the clicked {@link Sample}.
     * If the specified {@link Point} is inside a {@link Sample},
     * that {@link Sample}'s curvature line is toggled, and returns true.
     * Otherwise, just returns false.
     *
     * @param clicked the {@link Point} that the user clicked
     * @return true iff a curvature line was toggled
     * @see Sample#toggleCurvature()
     */
    private boolean toggleCurvature(final Point clicked) {
        for (Sample sample : samples) {
            if (sample.contains(clicked)) {
                sample.toggleCurvature();
                return true;
            }
        }
        return false;
    }

    /** Activates all tangents on the {@link Sample}s. */
    public void showTangents() {
        samples.forEach(sample -> sample.setTangentActivated(true));
    }

    /** Deactivates all tangents on the {@link Sample}s. */
    public void hideTangents() {
        samples.forEach(sample -> sample.setTangentActivated(false));
    }

    /** Activates all curvatures on the {@link Sample}s. */
    public void showCurvatures() {
        samples.forEach(sample -> sample.setCurvatureActivated(true));
    }

    /** Deactivates all curvatures on the {@link Sample}s. */
    public void hideCurvatures() {
        samples.forEach(sample -> sample.setCurvatureActivated(false));
    }

    /** Removes all {@link ControlPoint} for this {@link BezierCurve}. */
    public void removeAllControlPoints() {
        controlPoints.clear();
        APP.setMessage("All Control Points were removed!");
        update();
    }

    /**
     * Moves the specified {@link ControlPoint} to the target point.
     *
     * @param cp     the {@link ControlPoint} to move
     * @param target the location to move the {@link Point} to
     */
    public void moveControlPoint(final ControlPoint cp, final Point target) {
        cp.setLocation(target);
        update();
    }

    /**
     * Updates the status of the curve. Specifically:
     * the curve segments, their total length, and the sample points.
     */
    private void update() {
        createCurve();
        calculateLength();
        sampleCurve(samples.size());
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

    /** Calculate the length of the Bezier Curve that will be drawn. */
    private void calculateLength() {
        length = 0;
        for (CurveSegment segment : curveSegments) {
            length += segment.length();
        }
    }

    /**
     * Samples the curve uniformly,
     * filling the {@link BezierCurve#samples} list.
     *
     * @param numberOfSamples the amount of {@link Sample}s to make
     */
    public void sampleCurve(final int numberOfSamples) {
        samples.clear();
        if (numberOfSamples < 1) {
            return;
        }

        // The distance between two consecutive samples.
        double sampleDist = length / (numberOfSamples + 1d);
        double distanceMoved = 0;
        for (CurveSegment segment : curveSegments) {
            distanceMoved += segment.length();
            if (distanceMoved >= sampleDist) {
                Sample sample = new Sample(segment, samples.size());
                samples.add(sample);
                distanceMoved -= sampleDist;
                if (samples.size() == numberOfSamples) {
                    break;
                }
            }
        }
    }

    /**
     * Retrieves the {@link ControlPoint} that has been pressed.
     * If a {@link ControlPoint} is pressed, it is returned.
     * Otherwise, null is returned.
     *
     * @param pressed the {@link Point} that the user pressed
     * @return the pressed {@link ControlPoint}
     * or null if no {@link ControlPoint} is pressed
     */
    public ControlPoint getPressedControlPoint(final Point pressed) {
        for (ControlPoint cp : controlPoints) {
            if (cp.contains(pressed)) {
                return cp;
            }
        }
        return null;
    }

    /**
     * Creates and returns a {@link String} representation of the curve.
     *
     * @return the {@link String} representation of the {@link BezierCurve}
     */
    @Override
    public String toString() {
        return "BezierCurve{"
                + "controlPoints=" + controlPoints + ","
                + "samples=" + samples + ","
                + "}";
    }

    /**
     * Paints this {@link BezierCurve}
     * using the given {@link Graphics2D} object.
     *
     * @param g the {@link Graphics2D} object to use
     */
    public void paint(final Graphics2D g) {
        paintControlPoints(g);
        paintSamplePoints(g);
        paintCurve(g);
    }

    /**
     * Paints the {@link ControlPoint}s of this {@link BezierCurve}
     * using the given {@link Graphics2D} object.
     *
     * @param g the {@link Graphics2D} object to use
     */
    private void paintControlPoints(final Graphics2D g) {
        int i = 0;
        for (ControlPoint controlPoint : controlPoints) {
            controlPoint.paint(g, String.valueOf(i));
            i++;
        }
    }

    /**
     * Paints the {@link Sample}s on this {@link BezierCurve}
     * using the given {@link Graphics2D} object.
     *
     * @param g the {@link Graphics2D} object to use
     */
    private void paintSamplePoints(final Graphics2D g) {
        if (controlPoints.size() < 2) {
            return;
        }
        for (Sample sample : samples) {
            sample.paint(g);
        }
    }

    /**
     * Paints the curve of this {@link BezierCurve}
     * using the given {@link Graphics2D} object.
     *
     * @param g the {@link Graphics2D} object to use
     * @see BezierCurve#curveSegments
     */
    private void paintCurve(final Graphics2D g) {
        if (controlPoints.size() < 2) {
            return;
        }
        if (APP.segmentsAreInvisible()) {
            return;
        }
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(CURVE_COLOR);
        for (CurveSegment segment : curveSegments) {
            g.draw(segment);
        }
    }

    /**
     * Calculates and returns a {@link Point2D.Double}
     * on this {@link BezierCurve}.
     * The parameter is a proportion from 0 to 1.
     *
     * @param u the proportion from 0 to 1
     * @return the {@link Point2D.Double}
     */
    private Point2D.Double calculatePoint(final double u) {
        return applyFormula(n -> i ->
                Combinatorics.bernsteinCoefficient(n, i, u));
    }

    /**
     * Creates the Bezier Curve that can be drawn on the screen.
     * The curve is made up of {@link CurveSegment}s.
     * Each segment connects two consecutive points.
     */
    private void createCurve() {
        if (controlPoints.size() < 2) {
            return;
        }
        curveSegments.clear();
        for (double i = 0; i < NUMBER_OF_POINTS; i++) {
            double u1 = i / NUMBER_OF_POINTS;
            Point2D.Double pu1 = calculatePoint(u1);
            double u2 = (i + 1) / NUMBER_OF_POINTS;
            Point2D.Double pu2 = calculatePoint(u2);
            CurveSegment segment = new CurveSegment(pu1, pu2, u2);
            curveSegments.add(segment);
        }
    }

    /**
     * Calculates and returns the tangent vector
     * from a point on this {@link BezierCurve}.
     * This vector originates at the {@link Point2D.Double} from the same u.
     * That is, tangent vector T originates at point P.
     * The tangent line goes through points P and (P+T).
     * The parameter is a proportion from 0 to 1.
     *
     * @param u the proportion from 0 to 1
     * @return the tangent vector from the point at u
     */
    public Point2D.Double calculateTangent(final double u) {
        return applyFormula(n -> i ->
                Combinatorics.bernsteinDerivative(n, i, u));
    }

    /**
     * Calculates and returns the curvature vector
     * from a point on this {@link BezierCurve}.
     * This vector originates at the {@link Point2D.Double} from the same u.
     * That is, curvature vector C originates at point P.
     * The line goes through point P and (P+C).
     * The parameter is a proportion from 0 to 1.
     *
     * @param u the proportion from 0 to 1
     * @return the curvature vector from the point at u
     */
    public Point2D.Double calculateSecondDerivative(final double u) {
        // Remember that curvature is in direction opposite of normal.
        return applyFormula(n -> i ->
                Combinatorics.bernsteinSecondDerivative(n, i, u));
    }

    /**
     * Calculates and returns the point which is the result of
     * applying the given formula to all {@link ControlPoint}s of
     * this {@link BezierCurve}.
     * It is expected that the function returns coefficients for a polynomial
     * C0 * p0 + C1 * p1 + ... CN * pN
     * where pI are the control points
     * and CI are the coefficients returned by the {@link Function}.
     * The function is supplied 2 arguments:
     * n - the last index of the control points
     * i - the index for which the coefficient should be returned.
     *
     * @param formula the formula to apply
     * @return the resulting point
     */
    private Point2D.Double applyFormula(
            final Function<Integer, Function<Integer, Double>> formula) {
        Point2D.Double p = new Point2D.Double();
        int n = controlPoints.size() - 1;
        int i = 0;
        for (ControlPoint cp : controlPoints) {
            double coefficient = formula.apply(n).apply(i);
            p.x += coefficient * cp.getX();
            p.y += coefficient * cp.getY();
            i++;
        }
        return p;
    }

}

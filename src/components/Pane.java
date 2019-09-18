package components;

import components.bezier.ControlPoint;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static components.BezierCurve.CURVE;
import static main.Main.CONFIG;
import static main.Application.APP;

/**
 * The drawing area of the system.
 *
 * @author 150009974
 * @version 1.1
 */
public final class Pane extends JPanel {

    /** The main canvas in the interactive window. */
    public static final Pane PANE = new Pane();

    /** The control point being dragged. */
    private ControlPoint heldPoint;

    /**
     * Constructs an empty {@link Pane}
     * and adds a {@link MouseAdapter}s to catch mouse interaction.
     */
    private Pane() {
        this.setLayout(null);
        this.setFocusable(true);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1
                        && e.getButton() != MouseEvent.BUTTON3) {
                    // Ignore clicks that are not left or right.
                    return;
                }

                boolean left = e.getButton() == MouseEvent.BUTTON1;
                CURVE.processClick(left, e.getPoint());
                repaint();
            }

            @Override
            public void mousePressed(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    heldPoint = CURVE.getPressedControlPoint(e.getPoint());
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    heldPoint = null;
                }
                grabFocus();
            }
        });

        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(final MouseEvent e) {
                if (heldPoint != null) {
                    CURVE.moveControlPoint(heldPoint, e.getPoint());
                    repaint();
                }
            }
        });

        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(final KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_SPACE) {
                    CURVE.removeAllControlPoints();
                    repaint();
                }
            }
        });
    }

    /** Configures the main canvas in the interactive window. */
    public static void configureCanvas() {
        int x = (int) (CONFIG.getDouble("canvas x") * APP.getWidth());
        int y = (int) (CONFIG.getDouble("canvas y") * APP.getHeight());
        int w = (int) (CONFIG.getDouble("canvas width") * APP.getWidth());
        int h = (int) (CONFIG.getDouble("canvas height") * APP.getHeight());
        Color background = CONFIG.getColor("canvas background");
        Color border = CONFIG.getColor("canvas border");
        int thickness = CONFIG.getAnyInt("canvas border thickness");
        PANE.setLocation(x, y);
        PANE.setSize(w, h);
        PANE.setBorder(BorderFactory.createLineBorder(border, thickness));
        PANE.setBackground(background);
    }

    /**
     * Overrides the default paint method, so that
     * the {@link BezierCurve#CURVE} is painted.
     *
     * @param g the {@link Graphics} object to use for painting
     */
    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        CURVE.paint((Graphics2D) g);
    }

}

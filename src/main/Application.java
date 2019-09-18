package main;

import components.SamplesField;
import components.SwitchBoard;

import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.WindowConstants;
import javax.swing.SwingConstants;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Font;

import static components.BezierCurve.CURVE;
import static components.Pane.PANE;
import static main.Main.CONFIG;

/**
 * Represents an interactive window of the system.
 *
 * @author 150009974
 * @version 2.1
 */
public final class Application extends JDialog {

    /** The main window through which the user interacts. */
    public static final Application APP = new Application();

    /** The field to input number of samples. */
    private SamplesField sampleInput = new SamplesField();

    /** A {@link SwitchBoard} for the various switches of the app. */
    private SwitchBoard board = new SwitchBoard();

    /** The currently displayed message. */
    private JLabel message = new JLabel();

    /** Creates an application window with default configuration. */
    private Application() {
        config();
        configMessage();
        configSampleInput();
    }

    /** Applies the default configuration of the Application. */
    private void config() {
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setTitle("Bezier Curves");
        this.setUndecorated(true);
        Rectangle appBounds = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getMaximumWindowBounds();
        this.setBounds(appBounds);
    }

    /** Configures the label for displaying messages. */
    private void configMessage() {
        int w = (int) (CONFIG.getDouble("message width") * getWidth());
        int h = (int) (CONFIG.getDouble("message height") * getHeight());
        message.setSize(w, h);

        int x = (int) (CONFIG.getDouble("message x") * getWidth());
        int y = (int) (CONFIG.getDouble("message y") * getHeight());
        message.setLocation(x, y);

        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setVerticalAlignment(SwingConstants.CENTER);
        int size = CONFIG.getAnyInt("message size");
        String name = CONFIG.getString("message font");
        message.setFont(new Font(name, Font.BOLD, size));

        setMessage("Click on the canvas\nto enter a control point.");

        this.add(message);
    }

    /** Configures the field to input number of samples. */
    private void configSampleInput() {
        int w = (int) (CONFIG.getDouble("sample input width") * getWidth());
        int h = (int) (CONFIG.getDouble("sample input height") * getHeight());
        sampleInput.setSize(w, h);

        int x = (int) (CONFIG.getDouble("sample input x") * getWidth());
        int y = (int) (CONFIG.getDouble("sample input y") * getHeight());
        sampleInput.setLocation(x, y);

        this.add(sampleInput);
    }

    /** Configures the {@link SwitchBoard} of this {@link Application}. */
    public void configureSwitchBoard() {
        board.addSwitch("Bezier Curve", true);
        board.addSwitch("Tangents", true,
                e -> CURVE.showTangents(), e -> CURVE.hideTangents());
        board.addSwitch("Curvatures", false,
                e -> CURVE.showCurvatures(), e -> CURVE.hideCurvatures());
        board.addSwitch("Control Points", true);
        board.addSwitch("Control Point IDs", true);
        board.addSwitch("Samples", false);
        board.addSwitch("Sample IDs", false);

        int x = PANE.getX() + PANE.getWidth();
        int y = sampleInput.getY() + sampleInput.getHeight();
        int width = this.getWidth() - x;
        int height = this.getHeight() - y;
        board.setLocation(x, y);
        board.setSize(width, height);
        this.add(board);
    }

    /** @return true iff the tangents should be drawn */
    public boolean tangentsAreVisible() {
        return board.isOn("Tangents");
    }

    /** @return true iff the curvatures should be drawn */
    public boolean curvaturesAreVisible() {
        return board.isOn("Curvatures");
    }

    /** @return true iff the control point IDs should be drawn */
    public boolean controlPointsIDsAreVisible() {
        return board.isOn("Control Point IDs");
    }

    /** @return true iff the control point should be drawn */
    public boolean controlPointsAreVisible() {
        return board.isOn("Control Points");
    }

    /** @return true iff the sample IDs should be drawn */
    public boolean sampleIDsAreVisible() {
        return board.isOn("Sample IDs");
    }

    /** @return true iff the sample should be drawn */
    public boolean samplesAreVisible() {
        return board.isOn("Samples");
    }

    /** @return true iff the segments of the curve should NOT be drawn */
    public boolean segmentsAreInvisible() {
        return !board.isOn("Bezier Curve");
    }

    /**
     * Sets the text of the message label to the given text.
     * This method first replaces all new lines (\n) with "br" tags
     * and encloses the result in HTML before
     * invoking the label's {@link JLabel#setText(String)} method.
     *
     * @param text the message to display
     */
    public void setMessage(final String text) {
        String formatted = "<html>" + text.replaceAll("\n", "<br>")
                + "</html>";
        message.setText(formatted);
    }

}

package components;

import javax.swing.JTextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

import static components.BezierCurve.CURVE;
import static components.Pane.PANE;
import static main.Application.APP;

/**
 * Represents a field for input of number of samples.
 *
 * @author 150009974
 * @version 1.3
 */
public class SamplesField extends JTextField {

    /**
     * Constructs a {@link SamplesField} that
     * manages the number of {@link components.bezier.Sample}s
     * on a {@link BezierCurve}.
     */
    public SamplesField() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent event) {
                String amount = getText();
                try {
                    int number;
                    if (amount.isEmpty()) {
                        number = 0;
                    } else {
                        number = Integer.parseInt(amount);
                    }
                    if (number < 0) {
                        throw new NumberFormatException();
                    }
                    APP.setMessage("Sample size parsed successfully!\n"
                            + "Left/Right-click on a sample ;)");
                    CURVE.sampleCurve(number);
                } catch (NumberFormatException e) {
                    APP.setMessage("Could not parse number.\n"
                            + "Try entering a non-negative integer.");
                } finally {
                    APP.repaint();
                }
                if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                    PANE.grabFocus();
                }
            }
        });
    }

}

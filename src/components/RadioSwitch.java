package components;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

/**
 * Represents a pair of radio buttons that change a switch On and Off.
 *
 * @author 150009974
 * @version 1.0
 */
public class RadioSwitch extends JPanel {

    /** The label in front of the switch. */
    private JLabel label = new JLabel();

    /** The On button. */
    private JRadioButton on = new JRadioButton("On");

    /** The Off button. */
    private JRadioButton off = new JRadioButton("Off");

    /**
     * Creates a {@link RadioSwitch} with the given text.
     * If the initialState is true, the {@link RadioSwitch} is on,
     * otherwise it's off.
     *
     * @param text         the text in front of the {@link RadioSwitch}
     * @param initialState the initial state of the {@link RadioSwitch}
     */
    public RadioSwitch(final String text, final boolean initialState) {
        label.setText(text);
        on.setSelected(initialState);
        off.setSelected(!initialState);
        ButtonGroup pair = new ButtonGroup();
        pair.add(on);
        pair.add(off);

        this.setLayout(new FlowLayout());
        this.add(label);
        this.add(on);
        this.add(off);
    }

    /**
     * Adds a method to call when this {@link RadioSwitch} is turned on.
     *
     * @param method the method to call
     */
    public void whenSwitchedOn(final Consumer<ActionEvent> method) {
        on.addActionListener(method::accept);
    }

    /**
     * Adds a method to call when this {@link RadioSwitch} is turned off.
     *
     * @param method the method to call
     */
    public void whenSwitchedOff(final Consumer<ActionEvent> method) {
        off.addActionListener(method::accept);
    }

    /** @return true iff this {@link RadioSwitch} is on */
    public boolean isOn() {
        return on.isSelected();
    }

}

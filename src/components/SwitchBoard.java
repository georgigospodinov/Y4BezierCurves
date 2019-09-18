package components;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.event.ActionEvent;

import java.util.LinkedHashMap;
import java.util.function.Consumer;

import static components.Pane.PANE;
import static main.Application.APP;

/**
 * Represents a board of {@link RadioSwitch}es for an {@link main.Application}.
 *
 * @author 150009974
 * @version 1.0
 */
public class SwitchBoard extends JPanel {

    /** A map of all switches on this {@link SwitchBoard}. */
    private LinkedHashMap<String, RadioSwitch> switches = new LinkedHashMap<>();

    /** Creates an empty {@link SwitchBoard}. */
    public SwitchBoard() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    /**
     * Creates a stores a {@link RadioSwitch} with the specified name
     * and the specified initial state.
     *
     * @param name         the name of the {@link RadioSwitch}
     * @param initialState the initial state of the {@link RadioSwitch}
     */
    public void addSwitch(final String name, final boolean initialState) {
        RadioSwitch radioSwitch = new RadioSwitch(name, initialState);
        storeRadioSwitch(name, radioSwitch);
    }

    /**
     * Creates a stores a {@link RadioSwitch} with the specified name
     * and the specified initial state.
     * And adds the specified {@link Consumer}s to be called
     * when the {@link RadioSwitch} is switched.
     *
     * @param name         the name of the {@link RadioSwitch}
     * @param initialState the initial state of the {@link RadioSwitch}
     * @param whenOn       the method to execute
     *                     when the {@link RadioSwitch} is turned on
     * @param whenOff      the method to execute
     *                     when the {@link RadioSwitch} is turned off
     */
    public void addSwitch(final String name, final boolean initialState,
                          final Consumer<ActionEvent> whenOn,
                          final Consumer<ActionEvent> whenOff) {
        RadioSwitch radioSwitch = new RadioSwitch(name, initialState);
        radioSwitch.whenSwitchedOn(whenOn);
        radioSwitch.whenSwitchedOff(whenOff);
        storeRadioSwitch(name, radioSwitch);
    }

    /**
     * Maps the specified name to the given {@link RadioSwitch}
     * and adds it to this {@link SwitchBoard}.
     *
     * @param name        the unique name of the {@link RadioSwitch}
     * @param radioSwitch the {@link RadioSwitch} object
     */
    private void storeRadioSwitch(final String name,
                                  final RadioSwitch radioSwitch) {
        radioSwitch.whenSwitchedOn(event -> {
            PANE.grabFocus();
            APP.repaint();
        });
        radioSwitch.whenSwitchedOff(event -> {
            PANE.grabFocus();
            APP.repaint();
        });
        switches.put(name, radioSwitch);
        this.add(radioSwitch);
    }

    /**
     * Checks if the {@link RadioSwitch}, associated with the given name, is on.
     *
     * @param name the name of the {@link RadioSwitch}
     * @return true iff the respective {@link RadioSwitch} is on
     */
    public boolean isOn(final String name) {
        return switches.get(name).isOn();
    }

}

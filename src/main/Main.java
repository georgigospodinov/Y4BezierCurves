package main;

import util.PrintFormatting;
import util.GraphicsProps;

import java.io.FileNotFoundException;

import static main.Application.APP;
import static components.Pane.PANE;
import static components.Pane.configureCanvas;

/**
 * The main executable class.
 *
 * @author 150009974
 * @version 1.1
 */
public final class Main {

    /** The configuration properties. */
    public static final GraphicsProps CONFIG = new GraphicsProps();

    static {
        try {
            CONFIG.load("settings.props");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** Starts interaction with the user and wait until the window is closed. */
    private static void startInteraction() {
        APP.add(PANE);
        APP.configureSwitchBoard();
        APP.setModal(true);
        APP.setVisible(true);
    }

    /** Hides the constructor for this utility class. */
    private Main() {
    }

    /**
     * The main method to execute the system.
     * Any arguments passed are ignored.
     *
     * @param args the command line arguments (which get ignored)
     */
    public static void main(final String[] args) {
        configureCanvas();
        PrintFormatting.print("Configured!", "Starting interaction...");
        startInteraction();
        PrintFormatting.print("Interaction complete.");
    }
}

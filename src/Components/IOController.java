package Components;

import config.UserInterface;


/**
 * A controller for Input and Output instances.
 */
public class IOController implements UserInterface {
    /**
     * Instantiate the {@link Input} and {@link Output} classes.
     */
    protected IOController() {
        Input.getInstance();
        Output.getInstance();
    }
}

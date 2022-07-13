package IO;

import Factories.EntityFactory;


/**
 * A controller for Input and Output instances.
 */
public class IOController extends EntityFactory {
    /**
     * Instantiate the {@link Input} and {@link Output} classes.
     */
    protected IOController() {
        Input.getInstance();
        Output.getInstance();
    }
}

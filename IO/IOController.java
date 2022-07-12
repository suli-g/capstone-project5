package IO;

import Factories.EntityFactory;


/**
 * A controller for Input and Output instances.
 */
public class IOController extends EntityFactory {
    protected IOController() {
        Input.getInstance();
        Output.getInstance();
    }
}

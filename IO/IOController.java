package IO;

import Factories.EntityFactory;


/**
 * A controller for Input and Output instances.
 */
public class IOController extends EntityFactory {
    public IOController() {
        Input.getInstance();
        Output.getInstance();
    }
}

package IO;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import Factories.DataSourceFactory;
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

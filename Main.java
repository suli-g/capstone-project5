
/**
 * An application used to create and modify projects.
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Stack;

import Components.Menu;
import Entities.Person;
import Entities.Project;
import IO.App;
import IO.Input;
import IO.Output;

public class Main extends App {
    private static final String COMPANY_NAME = "POISED";
    private static final String PROJECTS_FILE = "data/projects.csv";
    public static void main(String[] args) {
        Output.printHeader(COMPANY_NAME);
        String selectedOption;
        do {
            selectedOption = selectFromMenu();
            System.out.println(selectedOption);
            break;
        }
        while (true);
        System.out.println("Goodbye!");
    }
}
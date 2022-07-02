
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

public class Main {
    private static final String COMPANY_NAME = "POISED";
    private static final String PROJECTS_FILE = "data/projects.csv";
    private static App app;
    private static Stack<Menu> eventStack;
    private static BufferedWriter buffer; 
    private static FileWriter file;
    public static void main(String[] args) {
        app = App.getInstance();

        String selectedOption;
        Output.printHeader(COMPANY_NAME);
        Menu currentMenu;
        do {
            currentMenu = app.getMenu();
            System.out.println(currentMenu);
            selectedOption = Input.expect("").toString();
            if (selectedOption == "q") {
                eventStack.pop();
            } else if (currentMenu.containsKey(selectedOption)){
                currentMenu.run(selectedOption);
            }
        }
        while (!eventStack.empty());
        System.out.println("Goodbye!");
    }

    private static void loadProjects(Path path) throws IOException {
        try {
            Files.createDirectory(path.getRoot());
        } 
        catch(FileAlreadyExistsException exists) {
            System.out.println("The directory already exists.");
        }

    }

    private static void initializeBuffer() {
        Path rootPath = Path.of(PROJECTS_FILE)
        try {

            Files.createDirectories(rootPath.getRoot());
        } catch(IOException error) {
            System.out.println("Something went wrong while accessing the projects file.");
            System.out.println(error);
        }
    }

    // private static void showMainMenu(Project project) throws IllegalArgumentException {
    //     boolean hasOutstanding = false;
    //     String selected;
    //     while (true) {
    //         System.out.println(project);
    //         System.out.println(PROJECT_MENU);
    //         try {
    //             selected = Input.expect("").toString();
    //         } catch(IllegalStateException error) {
    //             System.out.println(error);
    //             continue;
    //         }
    //         switch (selected) {
    //             case "c":
    //                 setProjectDueDate(project);
    //                 break;
    //             case "p":
    //                 project.setPaid(Input.expect("Total money paid").toDouble());
    //                 break;
    //             case "f":
    //                 hasOutstanding = project.markFinalized();
    //                 if (hasOutstanding) {
    //                     System.out.println(project.getInvoice());
    //                 } else {
    //                     project.markFinalized();
    //                     System.out.println("The project was finalized on " + project.dateFinalized());
    //                 }
    //                 break;
    //             case "d":
    //                 // Falls through if user enters 'q' in submenu.
    //                 if (showSubMenu(project.getContractor())) {
    //                     break;
    //                 }
    //             case "q":
    //                 return;
    //             default:
    //                 System.out.println("Incorrect option entered.");
    //         }
    //     }
    // }

    // private static boolean showSubMenu(Person contractor) throws IllegalArgumentException {
    //     String selected;
    //     while (true) {
    //         try {
    //             selected = Input.expect("").toString();
    //         } catch(IllegalStateException error) {
    //             System.out.println(error);
    //             continue;
    //         }
    //         System.out.println(PERSONNEL_MENU);
    //         switch (selected) {
    //             case "e":
    //                 contractor.setEmailAddress(Input.query("New email address").toString());
    //                 break;
    //             case "p":
    //                 contractor.setPhoneNumber(Input.query("New phone number").toInteger());
    //                 break;
    //             case "b":
    //                 return true;
    //             case "q":
    //                 return false;
    //             default:
    //                 System.out.println("Incorrect option entered.");
    //         }
    //     }
    // }
}
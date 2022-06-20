
/**
 * An application used to create and modify projects.
 */
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.NoSuchElementException;

import Components.Menu;
import Entities.Person;
import Entities.Project;
import IO.IOController;
import IO.Input;
import IO.Output;

public class Main extends IOController {
    private static final String COMPANY_NAME = "POISED";
    private static final HashMap<String, String> PROJECT_OPTIONS = new HashMap<>(){{
        put("c", "Change due date");
        put("p", "Change amount paid");
        put("d", "Change a contractor's details");
        put("f", "Finalize the project");
        put("q", "Quit application");
    }};

    private static final HashMap<String, String> PERSONNEL_OPTIONS = new HashMap<>(){{
        put("e", "Change Email Address");
        put("p", "Change Phone Number");
        put("b", "Back to main Menu");
        put("q", "Quit Application");
    }};

    private static final Menu PERSONNEL_MENU = new Menu("Personnel", PERSONNEL_OPTIONS);
    private static final Menu PROJECT_MENU = new Menu("Main", PROJECT_OPTIONS);

    public Main() {
        super();
    }

    public static void main(String[] args) {
        Output.printHeader(COMPANY_NAME);
        Project project = newProject();
        Person contractor = newPerson("Contractor"),
                customer = newPerson("Customer"),
                architect = newPerson("Architect");


        project.setPaid(Input.expect("Total amount paid").toDouble())
                .setCustomer(customer)
                .setArchitect(architect)
                .setContractor(contractor);
        setProjectDueDate(project);
        showMainMenu(project);
        System.out.println("Goodbye!");
    }

    private static void showMainMenu(Project project) throws IllegalArgumentException {
        boolean hasOutstanding = false;
        String selected;
        while (true) {
            System.out.println(project);
            System.out.println(PROJECT_MENU);
            try {
                selected = Input.expect("").toString();
            } catch(IllegalStateException error) {
                System.out.println(error);
                continue;
            }
            switch (selected) {
                case "c":
                    setProjectDueDate(project);
                    break;
                case "p":
                    project.setPaid(Input.expect("Total money paid").toDouble());
                    break;
                case "f":
                    hasOutstanding = project.markFinalized();
                    if (hasOutstanding) {
                        System.out.println(project.getInvoice());
                    } else {
                        project.markFinalized();
                        System.out.println("The project was finalized on " + project.dateFinalized());
                    }
                    break;
                case "d":
                    // Falls through if user enters 'q' in submenu.
                    if (showSubMenu(project.getContractor())) {
                        break;
                    }
                case "q":
                    return;
                default:
                    System.out.println("Incorrect option entered.");
            }
        }
    }

    private static boolean showSubMenu(Person contractor) throws IllegalArgumentException {
        String selected;
        while (true) {
            try {
                selected = Input.expect("").toString();
            } catch(IllegalStateException error) {
                System.out.println(error);
                continue;
            }
            System.out.println(PERSONNEL_MENU);
            switch (selected) {
                case "e":
                    contractor.setEmailAddress(Input.query("New email address").toString());
                    break;
                case "p":
                    contractor.setPhoneNumber(Input.query("New phone number").toInteger());
                    break;
                case "b":
                    return true;
                case "q":
                    return false;
                default:
                    System.out.println("Incorrect option entered.");
            }
        }
    }

    /**
     * Safely sets the due date for a Project.
     * 
     * @param project
     */
    private static void setProjectDueDate(Project project) {
        System.out.println("Setting due date");
        boolean incorrectDateFormat = true;
        String dueDate;
        while(incorrectDateFormat) {
            try {
                dueDate = Input.expect("Due date (yyyy-mm-dd)").toString();
                project.setDueDate(dueDate);
                incorrectDateFormat = false;
            } catch(DateTimeParseException error) {
                System.out.println("Incorrect format given. The date should contain only numbers.");
                System.out.println("Example: 2021-11-12");
            } catch(IllegalArgumentException error) {
                System.out.println(error);
            } catch(IllegalStateException error) {
                System.out.println(error);
            }
        }
    }
}
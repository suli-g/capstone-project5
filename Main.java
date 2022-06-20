
/**
 * An application used to create and modify projects.
 */
import java.time.format.DateTimeParseException;
import java.util.HashMap;

import Components.Menu;
import Controller.Controller;
import Controller.Interactions.Input;
import Controller.Interactions.Output;
import Entities.Person;
import Entities.Project;

public class Main extends Controller {
    private static final char NODE_CHAR = '+', EDGE_CHAR = '-';
    private static final String COMPANY_NAME = "POISED";
    private static final int PAGE_WIDTH = 70;

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
        printLogo();
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
        while (true) {
            printLogo();
            System.out.println(project);
            System.out.println(PROJECT_MENU);
            switch (Input.expect("").toString()) {
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
                        System.out.println("The project finalized on " + project.dateFinalized());
                    }
                    break;
                case "d":
                    // Fall through if user enters 'q' in submenu.
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
        while (true) {
            System.out.println(PERSONNEL_MENU);
            switch (Input.expect("").toString()) {
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
                System.out.println(error.getLocalizedMessage());
            }
        }
    }

    /**
     * Prints a logo at the start of the application. Cosmetic
     */
    private static void printLogo() {
        String logoText = String.format("+++++{ %s }+++++", COMPANY_NAME),
                headingText = "[{ Project Management System }]",
                horizontalLine = Output.drawHorizontalLine(EDGE_CHAR, PAGE_WIDTH);

        System.out.println(horizontalLine);
        System.out.println(Output.embedTextInLine(logoText, horizontalLine));
        System.out.println(Output.embedTextInLine(headingText, horizontalLine));
        System.out.println(horizontalLine);
    }
}
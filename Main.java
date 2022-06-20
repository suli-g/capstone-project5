
/**
 * An application used to create and modify projects.
 */
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

import Components.Menu;
import Components.Prompter;
import Entities.Person;
import Entities.Project;
import Factories.EntityFactory;

public class Main extends EntityFactory {
    private static final String COMPANY_NAME = "POISED";
    private static final int PAGE_WIDTH = 70;
    private static final char BORDER_CHAR = '-', CORNER_CHAR = '+';

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

        project.setPaid(Prompter.expect("Total amount paid").toDouble())
                .setCustomer(customer)
                .setArchitect(architect)
                .setContractor(contractor);
        setDueDate(project);
        showMainMenu(project);
        System.out.println("Goodbye!");
    }

    private static void showMainMenu(Project project) throws IllegalArgumentException {
        boolean hasOutstanding = false;
        while (true) {
            printBorder(BORDER_CHAR, CORNER_CHAR, "");
            System.out.println(project);
            System.out.println(PROJECT_MENU);
            switch (Prompter.expect("").toString()) {
                case "c":
                    setDueDate(project);
                    break;
                case "p":
                    project.setPaid(Prompter.expect("Total money paid").toDouble());
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
            switch (Prompter.expect("").toString()) {
                case "e":
                    contractor.setEmailAddress(Prompter.query("New email address").toString());
                    break;
                case "p":
                    contractor.setPhoneNumber(Prompter.query("New phone number").toInteger());
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
    private static void setDueDate(Project project) {
        System.out.println("Setting due date");
        boolean incorrectDateFormat = true;
        LocalDate dateGiven = null;
        while(incorrectDateFormat) {
            try {
                dateGiven = LocalDate.parse(Prompter.expect("Due date (yyyy-mm-dd)").toString());
                incorrectDateFormat = false;
            } catch(DateTimeParseException error) {
                System.out.println("Incorrect date format.");
            }
        }
        project.setDueDate(dateGiven.getYear(), dateGiven.getMonthValue(), dateGiven.getDayOfMonth());
    }

    /**
     * Prints a little logo for Poised.
     */
    private static void printBorder(char edgeChar, char nodeChar, String text) {
        int textLength = text.length(),
                gapIndex = (int) (PAGE_WIDTH - text.length()) / 2;

        for (int i = 0; i < PAGE_WIDTH; i++) {
            if (i == 0) {
                System.out.print(nodeChar);
            } else if (i == gapIndex) {
                System.out.print(text);
                i += textLength;
            } else if (i == PAGE_WIDTH - 1) {
                System.out.println(nodeChar);
            } else {
                System.out.print(edgeChar);
            }
        }
    }

    /**
     * Prints a logo at the start of the application. Cosmetic
     */
    private static void printLogo() {
        String logoText = String.format("{ %s }", COMPANY_NAME),
                headingText = "Project Management System";
        printBorder(BORDER_CHAR, CORNER_CHAR, logoText);
        printBorder(' ', ':', headingText);
        printBorder(BORDER_CHAR, CORNER_CHAR, "");
    }
}
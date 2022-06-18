
/**
 * An application used to create and modify projects.
 */
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import Entities.Person;
import Entities.Project;

public class Main {
    private static final String COMPANY_NAME = "POISED";
    private static Scanner input = new Scanner(System.in);
    private static final int PAGE_WIDTH = 70;
    private static final char BORDER_CHAR = '-', CORNER_CHAR = '+';
    private static ArrayList<String> projectMenu = new ArrayList<String>();
    static {
        projectMenu.add("[c]: Change due date");
        projectMenu.add("[p]: Change amount paid");
        projectMenu.add("[d]: Change a contractor's details");
        projectMenu.add("[f]: Finalize the project");
        projectMenu.add("[q]: Quit application");
    };

    private static ArrayList<String> modificationMenu = new ArrayList<String>() {
        {
            add("[e]: Change Email Address");
            add("[p]: Change Phone Number");
            add("[b]: Back to main Menu");
            add("[q]: Quit Application");
        }
    };

    public static void main(String[] args) {
        printLogo();
        Project project = newProject();

        Person contractor = newPerson("Contractor"),
                customer = newPerson("Customer"),
                architect = newPerson("Architect");

        project.setPaid(queryDouble("How much money has already been paid?"))
                .setCustomer(customer)
                .setArchitect(architect)
                .setContractor(contractor);
        setDueDate(project);
        showMainMenu(project);
        System.out.println("Goodbye!");
    }

    private static void showMainMenu(Project project) {
        boolean hasOutstanding = false;
        while (true) {
            printBorder(BORDER_CHAR, CORNER_CHAR, "");
            System.out.println(project);
            switch (menuSelection(projectMenu)) {
                case "c":
                    setDueDate(project);
                    break;
                case "p":
                    project.setPaid(
                            queryDouble("How much money has already been paid?"));
                    break;
                case "f":
                    hasOutstanding = project.markFinalized();
                    if (hasOutstanding) {
                        System.out.println(project.getInvoice());
                    } else {
                        project.markFinalized();
                        System.out.println("The project finalized on " + project.dateFinalized());
                    }
                case "d":
                    // Fall through if user enters 'q' in submenu.
                    if (showSubMenu(project.getContractor())) {
                        break;
                    }
                case "q":
                    break;
                default:
                    System.out.println("Incorrect option entered.");
            }
        }
    }

    private static boolean showSubMenu(Person contractor) {
        while (true) {
            switch (menuSelection(modificationMenu)) {
                case "e":
                    contractor.setEmailAddress(
                            query("New email address (leave blank to cancel):"));
                    break;
                case "p":
                    contractor.setPhoneNumber(
                            queryInt("New phone number (leave blank to cancel):"));
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
     * Prints all options in a menu and lets the user select an option.
     * 
     * @param menu The menu arraylist to iterate over.
     * @return A query (see {@link #query(String)} )
     */
    private static String menuSelection(ArrayList<String> menu) {
        for (String option : menu) {
            System.out.println(option);
        }
        return query("");
    }

    /**
     * Safely sets the due date for a Project.
     * 
     * @param project
     */
    private static void setDueDate(Project project) {
        System.out.println("Setting due date");
        Integer year = null,
                month = null,
                maxDay, dayOfMonth = null;
        // Use while loops to keep asking the user until a valid value is entered.
        while (true) {
            year = queryInt("Year due");
            if (year < YearMonth.now().getYear()) {
                System.out.printf("The year %d has expired. Pick a later year.", year);
            } else {
                break;
            }
        }
        while (true) {
            month = queryInt("Month due (1 - 12)");
            if (month < 1 || month > 12) {
                System.out.println("Pick a number between 1 and 12");
            } else {
                break;
            }
        }
        maxDay = YearMonth.of(year, month).lengthOfMonth();
        while (true) {
            dayOfMonth = queryInt("Day of month due (1 - " + maxDay + ")");
            if (dayOfMonth < 1 || dayOfMonth > maxDay) {
                System.out.println("Pick a number between 1 and " + maxDay);
            } else {
                break;
            }
        }
        project.setDueDate(year, month, dayOfMonth);
    }

    /**
     * Queries the user for details and creates a new Project object.
     * 
     * @return The project created.
     */
    private static Project newProject() {
        String projectName = query("Project Name (Press 'Enter' to skip)").trim();
        String projectAddress = query("Project Address").trim();
        String projectType = query("Buliding Type").trim();
        int erfNumber = queryInt("ERF Number");
        double totalCost = queryDouble("Projected cost for project");
        return new Project(projectName, projectAddress, projectType, erfNumber, totalCost);
    }

    /**
     * Queries the user for details and creates a new person.
     * 
     * @param position
     * @return
     */
    private static Person newPerson(String position) {
        System.out.println("Setting details for " + position + ":");
        String firstName = query("First name");
        String lastName = query("Last name");
        String physicalAddress = query("Physical address");
        String emailAddress = query("Email address");
        Integer phoneNumber = null;
        while (true) {
            phoneNumber = queryInt("Phone number");
            if (phoneNumber >= 1000_000_000 || phoneNumber <= 99_999_999) {
                System.out.println("Phone number should have 10 digits!");
            } else {
                break;
            }
        }
        return new Person(firstName, lastName, physicalAddress, emailAddress, phoneNumber);
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

    /**
     * Asks the user a question and returns the response.
     * 
     * @param question The question to be asked.
     * @return User response.
     */
    private static String query(String question) {
        String response = "";
        try {
            System.out.print(question + ": ");
            response = input.nextLine();
        } catch (NoSuchElementException error) {
            System.out.println("\nProgram aborted prematurely.");
            System.exit(1);
        }
        return response.trim();
    }

    /**
     * Asks the user a question and converts the response to an integer.
     * 
     * @param question The question to be asked.
     * @return User response.
     */
    private static Integer queryInt(String question) {
        String response = "";
        while (true) {
            try {
                /*
                 * To do this with nextInt(), a nextLine() would be needed as well.
                 * (https://stackoverflow.com/a/7056782/2850190)
                 */
                response = query(question);
                return Integer.parseInt(response);
            } catch (NumberFormatException exception) {
                System.out.println("Expected Integer. Got " + response);
            }
        }
    }

    /**
     * Asks the user a question and converts the response to a double.
     * 
     * @param question The question to be asked.
     * @return User response.
     */
    private static Double queryDouble(String question) {
        String response = "";
        try {
            /*
             * To do this with nextDouble(), a nextLine() would be needed as well.
             * (https://stackoverflow.com/a/7056782/2850190)
             */
            response = query(question);
            return Double.parseDouble(response);
        } catch (NumberFormatException exception) {
            System.out.println("Expected Double. Got " + response);
            System.exit(1);
            return null;
        }
    }
}
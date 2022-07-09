
/**
 * An application used to create and modify projects.
 */
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.NoSuchElementException;

import Components.Menu;
import Entities.Person;
import Entities.Project;
import Factories.DataSourceFactory;
import Factories.EntityFactory;
import Factories.EntityModel;
import IO.DataSource;
import IO.IOController;
import IO.Input;
import IO.Output;

public class Main extends IOController {
    private static final String COMPANY_NAME = "POISED",
            ENTITY_DATA_DIRECTORY = "entity-data",
            ENTITY_DATA_DELIMITER = ", ",
            REUSE_PERSON_MESSAGE = "The information given matches some in the database. Use details for %s? (Y/n)";
    private static final Menu MAIN_MENU = new Menu("Project") {
        {
            put("l", "List all projects");
            put("n", "Create new project");
            put("q", "Quit application");
        }
    };

    private static final Menu PROJECT_MENU = new Menu("Project") {
        {
            put("c", "Change due date");
            put("p", "Change amount paid");
            put("d", "Change a contractor's details");
            put("f", "Finalize the project");
            put("q", "Quit application");
        }
    };

    private static final Menu PERSONNEL_MENU = new Menu("Personnel") {
        {
            put("e", "Change Email Address");
            put("p", "Change Phone Number");
            put("b", "Back to main Menu");
            put("q", "Quit Application");
        }
    };

    private static DataSource projects, people;

    private Main() {
        super();
    }

    public static void main(String[] args) {
        try {
            DataSourceFactory dataSourceFactory = DataSourceFactory.getInstance(ENTITY_DATA_DIRECTORY)
                    .add("people.csv")
                    .add("projects.csv");
            projects = dataSourceFactory.get("projects.csv");
            people = dataSourceFactory.get("people.csv");
            EntityModel.getInstance(projects, people, ENTITY_DATA_DELIMITER);
            Output.printHeader(COMPANY_NAME);
            showMainMenu();
            System.out.println("\nGoodbye!");
        } catch (NoSuchElementException error) {
            System.out.println("\nProgram execution aborted unexpectedly.");
        } catch (IOException error) {
            System.out.println("\nSomething went wrong while accessing the data sources.");
            System.out.println(error);
        }

    }

    private static void showMainMenu() throws IllegalArgumentException, NoSuchElementException {
        String selected;
        while (true) {
            System.out.println(MAIN_MENU);
            try {
                selected = Input.expect("").toString();
            } catch (IllegalStateException error) {
                System.out.println(error);
                continue;
            }
            switch (selected) {
                case "l":
                    String nextLine = "";
                    while (true) {
                        nextLine = projects.getLine();
                        if (nextLine == null) {
                            break;
                        }
                        System.out.println(nextLine);
                    }
                    break;
                case "n":
                    Person customer = createPerson("customer"),
                            contractor = createPerson("contractor"),
                            architect = createPerson("architect");
                    String projectName = Input.query("Project name").toString();
                    String projectType = Input.expect("Project Type").toString();
                    Project project;
                    if (projectName == "") {
                        project = createProject(projectType + " " + customer.getLastName(), projectType);
                    } else {
                        project = createProject(projectName, projectType);
                    }
                    project.setArchitect(architect)
                            .setContractor(contractor);
                    showProjectMenu(project);
                    break;
                case "q":
                    return;
                default:
                    System.out.println("Incorrect option entered.");
            }
        }
    }

    private static void showProjectMenu(Project project) throws IllegalArgumentException {
        boolean hasOutstanding = false;
        String selected;
        while (true) {
            System.out.println(project);
            System.out.println(PROJECT_MENU);
            try {
                selected = Input.expect("").toString();
            } catch (IllegalStateException error) {
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
                    if (showPersonnelMenu(project.getContractor())) {
                        break;
                    }
                case "q":
                    return;
                default:
                    System.out.println("Incorrect option entered.");
            }
        }
    }

    private static boolean showPersonnelMenu(Person contractor) throws IllegalArgumentException {
        String selected;
        while (true) {
            try {
                selected = Input.expect("").toString();
            } catch (IllegalStateException error) {
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

    private static Project createProject(String projectName, String projectType) throws NoSuchElementException {
        int erfNumber;
        String projectAddress;
        double projectCost;
        while (true) {
            try {
                Project thisProject = getProject(projectName);
                if (thisProject != null) {
                    /*
                     * Since project names ought to be unique, if the name matches
                     * then return the project with that name.
                     */
                    System.out.println("A project with the name '" + projectName + "' already exists.");
                    return thisProject;
                }
                erfNumber = Input.expect("Project ERF Number").toInteger();
                projectAddress = Input.expect("Project address").toString();
                projectCost = Input.expect("Project Cost").toDouble();
                return EntityModel.addProject(projectName, erfNumber, projectType, projectAddress, projectCost);
            } catch (IllegalStateException error) {
                // Just cancel the operation if a required field is skipped.
                System.out.println(error);
                continue;
            } catch (IOException error) {
                System.out.println("An error occurred while saving project details.");
                System.out.println(error);
                return null;
            }
        }

    }

    private static Person createPerson(String position) throws NoSuchElementException {
        System.out.println("Setting details for " + position + ": ");
        String firstName = null,
                lastName = null,
                physicalAddress = null,
                emailAddress = null;
        Integer phoneNumber = null;
        String useOld = null;
        Person thisPerson;
        while (true) {
            try {
                if (phoneNumber == null)
                    phoneNumber = Input.expect("Phone number").toInteger();
                thisPerson = getPerson(phoneNumber);
                /*
                 * Since phone numbers are unique, return a person if there is a
                 * phone number that matches.
                 */
                if (thisPerson != null) {
                    useOld = Input.query(String.format(REUSE_PERSON_MESSAGE, thisPerson.getName()))
                            .toString();
                    if (!useOld.equalsIgnoreCase("n")) {
                        return thisPerson;
                    } else {
                        phoneNumber = null;
                    }
                    continue;
                }
                if (firstName == null || firstName == "")
                    firstName = Input.expect("First name").toString();
                if (lastName == null || lastName == "")
                    lastName = Input.expect("Last name").toString();
                if (physicalAddress == null || physicalAddress == "")
                    physicalAddress = Input.expect("Physical address").toString();
                if (emailAddress == null || emailAddress == "")
                    emailAddress = Input.expect("Email address").toString();
                return EntityModel.addPerson(phoneNumber, firstName, lastName, physicalAddress, emailAddress);
            } catch (IllegalStateException error) {
                continue;
            } catch(IOException error) {
                System.out.println("An error occurred while saving details for " + position);
            }
        }
    }

    /**
     * Safely sets the due date for a Project.
     * 
     * @param project
     */
    private static void setProjectDueDate(Project project) {
        System.out.println("Setting due date for project: " + project.getName());
        Integer year = null, month = null, dayOfMonth = null;
        LocalDate dueDate = null;
        int temp;
        while (dueDate == null) {
            if (year == null) {
                temp = Input.expect("Year (yyyy): ").toInteger();
                if (LocalDate.now().getYear() < temp) {
                    year = temp;
                }
            } else if (month == null) {
                temp = Input.expect("Month (MM): ").toInteger();
                if (LocalDate.now().getMonthValue() < temp) {
                    month = temp;
                }
            } else if (dayOfMonth == null) {
                temp = Input.expect("Day of month (dd): ").toInteger();
                if (LocalDate.now().getDayOfMonth() < temp) {
                    dayOfMonth = temp;
                }
            } else {
                dueDate = LocalDate.of(year, month, dayOfMonth);
            }
        }
        project.setDueDate(dueDate.toString());
    }

}
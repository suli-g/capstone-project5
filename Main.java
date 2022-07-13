
/**
 * An application used to create and modify projects.
 */
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import Components.EntityModel;
import Components.Menu;
import Entities.Person;
import Entities.Project;
import Factories.DataSourceFactory;
import Factories.EntityFactory;
import IO.DataSource;
import IO.IOController;
import IO.Input;
import IO.Output;


class ProjectOverview {
    private static final String OVERVIEW_FORMAT = """
            project number:             %-8d
            project name:               %-8s
            project cost:               R%.2f
            total paid to date:         R%.2f
            customer name:              %-8s
            customer contact number:    %-8s
            completion status:          %-8s
            date finalized:             %-8s
            """;
    private int projectNumber;
    private Project project;

    public ProjectOverview(int projectNumber, Project project) {
        this.project = project;
        this.projectNumber = projectNumber;
    }

    public String toString() {
        Project.COMPLETION_STATUS status = project.getStatus();
        String dateFinalized = "n/a";
        if (status == Project.COMPLETION_STATUS.FINALIZED) {
            dateFinalized = project.getDateFinalized();
        }
        Person customer = project.getPerson("Customer");
        return new StringBuilder()
                .append("-")
                .append("\n")
                .append(String.format(OVERVIEW_FORMAT, 
                    projectNumber,
                    project.getName(),
                    project.getCost(),
                    project.getPaid(),
                    customer.getName(),
                    customer.getPhoneNumber(),
                    status.label,
                    dateFinalized
                    ))
                .append("-")
                .toString();
    }
}

public class Main extends IOController {
    private static final String COMPANY_NAME = "POISED",
            ENTITY_DATA_DIRECTORY = "entity-data",
            REUSE_PERSON_MESSAGE = "The information given matches some in the database. Use details for %s? (Y/n)";
    private static final Menu MAIN_MENU = new Menu("Project") {
        {
            put("list", "List all projects");
            put("create", "Create new project");
            put("select", "Select a project");
            put("quit", "Quit application");
        }
    };

    private static final Menu PROJECT_MENU = new Menu("Project") {
        {
            put("due", "Change due date");
            put("paid", "Change amount paid");
            put("contractor", "Change a contractor's details");
            put("finalize", "Finalize the project");
            put("back", "Back to Main Menu");
            put("quit", "Quit application");
        }
    };

    private static final Menu PERSONNEL_MENU = new Menu("Personnel") {
        {
            put("email", "Change Email Address");
            put("phone", "Change Phone Number");
            put("back", "Back to Main Menu");
            put("quit", "Quit Application");
        }
    };

    private static DataSource projects, people;
    private Main() {
        super();
    }

    public static void main(String[] args) {
        try {
            /*
             * Initial loading steps:
             * Create "projects.csv" and "people.csv" at ENTITY_DATA_DIRECTORY if they don't
             * already exist.
             */
            DataSourceFactory dataSourceFactory = DataSourceFactory.setRootDirectory(ENTITY_DATA_DIRECTORY)
                    .addDataSource("people.csv")
                    .addDataSource("projects.csv");
            projects = dataSourceFactory.getDataSource("projects.csv");
            people = dataSourceFactory.getDataSource("people.csv");
            Project.setRequiredRoles("Customer", "Contractor", "Architect");
            loadPeople(people);
            loadProjects(projects);
            EntityModel.getInstance(projects, people);
            // Initial loading complete.
            Output.printHeader(COMPANY_NAME);
            mainMenuLoop();
            saveData();
            System.out.println("\nGoodbye!");
        } catch (NoSuchElementException error) {
            System.out.println("\nProgram execution aborted unexpectedly.");
        } catch (IOException error) {
            System.out.println("\nSomething went wrong while accessing the data sources.");
            System.out.println(error);
        }
    }

    /**
     * Saves all Entity Objects created during execution to the files created during
     * initial loading.
     */
    private static void saveData() {
        try {
            EntityModel.unparsePeople(EntityFactory.getPeople());
            EntityModel.unparseProjects(EntityFactory.getProjects());
        } catch (IOException error) {
            System.out.println("Something went wrong while trying to save people.");
            System.out.println(error);
        }
    }

    /**
     * Creates {@link Entities.Project} instances using the text stored in
     * {@code dataSource}.
     * 
     * @param dataSource
     */
    private static void loadProjects(DataSource dataSource) {
        String nextLine;
        Project currentProject;
        do {
            nextLine = dataSource.readLine();
            if (nextLine == null) {
                break;
            }
            currentProject = EntityModel.parseProject(nextLine);
            if (currentProject != null) {
                EntityFactory.addProject(currentProject);
            }
        } while (true);
    }

    /**
     * Creates {@link Entities.Person} instances using the text stored in
     * {@code dataSource}.
     * 
     * @param dataSource
     */
    private static void loadPeople(DataSource dataSource) {
        String nextLine;
        Person currentPerson;
        do {
            nextLine = dataSource.readLine();
            currentPerson = EntityModel.parsePerson(nextLine);
            if (currentPerson != null) {
                EntityFactory.assignPerson(currentPerson);
            }
        } while (nextLine != null);
    }

    /**
     * Displays the main menu and uses user input to execute actions.
     * 
     * @throws IllegalArgumentException
     * @throws NoSuchElementException
     */
    private static void mainMenuLoop() throws NoSuchElementException {
        String selectedOption;
        Project selectedProject = null;
        // Used to propagate a "quit" selection in sub menus.
        boolean shouldQuit = false;
        while (true) {
            if (shouldQuit) {
                break;
            }
            try {
                if (selectedProject != null) {
                    // Show a Project-specific menu if a project has been selected.
                    shouldQuit = projectMenuLoop(selectedProject);
                    selectedProject = null;
                } else {
                    System.out.println(MAIN_MENU);
                }
                selectedOption = Input.expect("").toString();
                switch (selectedOption) {
                    case "select":
                        // Prompt the user for a project number and select the project with the given
                        // index if it exists.\
                        int projectNumber = Input.expect("Project number").toInteger();
                        selectedProject = EntityFactory.getProjectById(projectNumber);
                        if (selectedProject == null) {
                            System.out.println("Invalid project number entered.");
                        }
                        break;
                    case "list":
                        // List an overview for each project if any exist.
                        listProjects();
                        break;
                    case "create":
                        // Create people first so that a default name can be set on creation.
                        Person customer = createPerson("customer"),
                                contractor,
                                architect;
                        Project project;
                        String projectName = Input.query("Project name").toString();
                        String projectType = Input.expect("Project Type").toString();
                        if (projectName == "") {
                            project = createProject(projectType + " " + customer.getLastName(), projectType);
                        } else {
                            project = createProject(projectName, projectType);
                        }
                        if (project.getPerson("Customer") == null) {
                            project.set("Customer", customer);
                        }
                        if (project.getPerson("Architect") == null) {
                            architect = createPerson("contractor");
                            project.set("Architect", architect);
                        }
                        if (project.getPerson("Contractor") == null) {
                            contractor = createPerson("architect");
                            project.set("Contractor", contractor);
                        }
                        // default to "{Customer Last name} {Project type}" if no project name is given
    
                        // Use the result of projectMenuLoop to decide if mainMenuLoop should end as
                        // well.
                        shouldQuit = projectMenuLoop(project);
                        break;
                    case "quit":
                        shouldQuit = true;
                        break;
                    default:
                        System.out.println("Incorrect option entered.");
                }
            } catch(IllegalStateException error) {
                System.out.println(error.getLocalizedMessage());
                continue;
            }
            
        }
    }

    /**
     * Shows a project-specific menu, given a {@link Entities.Project} object.
     * 
     * @param project The project to desplay.
     * @return a boolean indicating whether the program should end or not.
     * @throws IllegalArgumentException
     */
    private static boolean projectMenuLoop(Project project) {
        String selected;
        while (true) {
            // Show the project's details and then the menu.
            System.out.println(project);
            System.out.println(PROJECT_MENU);
            try {
                selected = Input.expect("").toString();
            switch (selected) {
                case "due":
                    System.out.println("Setting due date for project: " + project.getName());
                    project.setDueDate(queryDate());
                    break;
                case "paid":
                    project.setPaid(Input.expect("Total money paid").toDouble());
                    break;
                case "finalize":
                    // Finalize the project if all details are set, otherwise print an invoice.
                    project.markFinalized();
                    if (project.getStatus() != Project.COMPLETION_STATUS.FINALIZED) {
                        System.out.println(project.getInvoice());
                    } else {
                        System.out.println("The project was finalized on " + project.getDateFinalized());
                    }
                    break;
                case "contractor":
                    // Falls through if user enters 'q' in submenu.
                    if (personnelMenuLoop(project.getPerson("contractor"))) {
                        return true;
                    }
                case "quit":
                    return true;
                case "back":
                    return false;
                default:
                    System.out.println("Incorrect option entered.");
            }
            } catch (IllegalStateException error) {
                System.out.println(error);
                continue;
            }
        } 
    }

    /**
     * Shows a person-specific menu, given a {@link Entities.Person} object.
     * 
     * @param person the person object whose details should be modified.
     * @return a boolean indicating whether the program should end or not.
     * @throws IllegalArgumentException
     */
    private static boolean personnelMenuLoop(Person person) {
        String selected;
        while (true) {
            // Show the person's details and then the menu.
            System.out.println(person);
            System.out.println(PERSONNEL_MENU);
            try {
                selected = Input.expect("").toString();
            } catch (IllegalStateException error) {
                System.out.println(error);
                continue;
            }
            switch (selected) {
                case "email":
                    person.setEmailAddress(Input.query("New email address").toString());
                    break;
                case "phone":
                    int newPhoneNumber;
                    try {
                        newPhoneNumber = Input.query("New phone number").toInteger(9);
                        person.setPhoneNumber(newPhoneNumber);
                    } catch (NumberFormatException error) {
                        System.out.println(error);
                    }
                    break;
                case "back":
                    return false;
                case "quit":
                    return true;
                default:
                    System.out.println("Incorrect option entered.");
            }
        }
    }

    /**
     * Creates a new {@link Entities.Project} object using {@link Factories.EntityFactory#addProject(String, int, String, String, double, double, String)}.
     * 
     * @param projectName the name of the project
     * @param projectType the type of the project
     * @return the project with {@code name == projectName}
     * @throws NoSuchElementException
     */
    private static Project createProject(String projectName, String projectType) throws NoSuchElementException {
        int erfNumber;
        String projectAddress,
                dateFinalized = "-";
        double projectCost, amountPaid;
        while (true) {
            Project thisProject = getProjectByName(projectName);
            if (thisProject != null) {
                /*
                 * Since project names ought to be unique, if the name matches
                 * then return the project with that name.
                 */
                System.out.println("A project with the name '" + projectName + "' already exists.");
                return thisProject;
            }
            try {
                erfNumber = Input.expect("Project ERF Number").toInteger();
                projectAddress = Input.expect("Project address").toString();
                projectCost = Input.expect("Project Cost").toDouble();
                amountPaid = Input.expect("Amount paid").toDouble();
            } catch (IllegalStateException error) {
                // Just cancel the operation if a required field is skipped.
                System.out.println(error.getLocalizedMessage());
                continue;
            } catch (NumberFormatException error) {
                System.out.println(error.getLocalizedMessage());
                continue;
            }
            break;
        }
        return EntityFactory.addProject(projectName, erfNumber, projectType, projectAddress, projectCost,
                amountPaid, dateFinalized);

    }

    /**
     * Creates a new {@link Entities.Person} object using {@link Factories.EntityFactory#addPerson(int, String, String, String, String)}.
     * 
     * @param role the role of the person in the project.
     * @return the person created.
     * @throws NoSuchElementException
     */
    private static Person createPerson(String role) throws NoSuchElementException {
        System.out.println("Setting details for " + role + ": ");
        String firstName = null,
                lastName = null,
                physicalAddress = null,
                emailAddress = null;
        Integer phoneNumber = null;
        Person thisPerson;
        while (true) {
            try {
                if (phoneNumber == null)
                    phoneNumber = Input.expect("Phone number").toInteger(9);
                thisPerson = getPerson(phoneNumber);
                /*
                 * Since phone numbers are unique, return a person if there is a
                 * phone number that matches.
                 */
                if (thisPerson != null) {
                        Input.query(String.format(REUSE_PERSON_MESSAGE, thisPerson.getName()));
                    if (Input.getInstance().toBoolean()) {
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
                return EntityFactory.addPerson(phoneNumber, firstName, lastName, physicalAddress, emailAddress);
            } catch (NumberFormatException error) {
                System.out.println("A number is expected here.");
            } catch (IllegalArgumentException error) {
                System.out.print(error.getLocalizedMessage());
                System.out.println("(with optional leading zero)");
            } catch (IllegalStateException error) {
                System.out.println(error.getLocalizedMessage());
                continue;
            }
        }
    }

    /**
     * Safely sets the due date for a Project.
     * 
     * @param project
     */
    private static String queryDate() {
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
        return dueDate.toString();
    }

    private static void listProjects() {
        ArrayList<Project> projectList = EntityFactory.getProjects();
        if (projectList.size() > 0) {
            ProjectOverview overview;
            for (int projectNumber = 0; projectNumber < projectList.size(); projectNumber++) {
                overview = new ProjectOverview(projectNumber, getProjectById(projectNumber));
                System.out.println(overview.toString());
            }
        } else {
            System.out.println("No projects have been saved.");
        }
    }
}
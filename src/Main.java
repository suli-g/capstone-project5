
/**
 * An application used to create and modify projects.
 */
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import Components.EntityModel;
import Components.Menu;
import Components.ProjectOverview;
import Entities.Person;
import Entities.Project;
import Factories.DataSourceFactory;
import Factories.EntityFactory;
import Factories.PersonFactory;
import Factories.ProjectFactory;
import IO.DataSource;
import IO.IOController;
import IO.Input;
import IO.Output;

public class Main extends IOController {
    private static final String COMPANY_NAME = "POISED",
            ENTITY_DATA_DIRECTORY = "data";
                private static final Menu MAIN_MENU = new Menu("Project") {
        {
            put("list", "List all projects");
            put("list -i", "List incomplete projects");
            put("list -o", "List outstanding projects");
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
            put("fix missing", "Enter details for missing participants");
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
            System.out.println(error.getLocalizedMessage());
        }
    }

    /**
     * Creates {@link Entities.Project} instances using the text stored in
     * {@code dataSource}.
     * 
     * @param dataSource
     */
    private static void loadProjects(DataSource dataSource) {
        int line = 0;
        String nextLine;
        Project currentProject;
        do {
            nextLine = dataSource.readLine();
            if (nextLine == null) {
                break;
            }
            try {
                currentProject = EntityModel.parseProject(nextLine);
                if (currentProject != null) {
                    EntityFactory.addProject(currentProject);
                }
            } catch(IndexOutOfBoundsException error) {
                System.out.printf("%s (line %d)\n", error, line);
            } 
            // catch(NumberFormatException error) {
            //     System.out.printf("%s (line %d)\n", error, line);
            // }
            line++;
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
        int line = 0;
        Person currentPerson;
        do {
            nextLine = dataSource.readLine();
            try {
                currentPerson = EntityModel.parsePerson(nextLine);
                if (currentPerson != null) {
                    EntityFactory.assignPerson(currentPerson);
                }
            } catch(IndexOutOfBoundsException error) {
                System.out.printf("%s (line %d)\n", error, line);
            } catch(NumberFormatException error) {
                System.out.printf("%s (line %d)\n", error, line);
            }

            line++;
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
        boolean continueLoop = true;
        while (continueLoop) {
            try {
                if (selectedProject != null) {
                    // Show a Project-specific menu if a project has been selected.
                    continueLoop = projectMenuLoop(selectedProject);
                    selectedProject = null;
                    continue;
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
                        ProjectFactory.list();
                        break;
                    case "list -i":
                        // List an overview for each project if any exist.
                        ProjectFactory.list(Project.COMPLETION_STATUS.IN_PROGRESS);
                        break;
                    case "list -o":
                        // List an overview for each project if any exist.
                        ProjectFactory.list(Project.COMPLETION_STATUS.OUTSTANDING);
                        break;
                    case "create":
                        // Create people first so that a default name can be set on creation.
                        Person customer = PersonFactory.create("Customer"),
                                contractor,
                                architect;
                        String projectName = Input.query("Project name").toString();
                        String projectType = Input.expect("Project Type").toString();
                        // default to "{Customer Last name} {Project type}" if no project name is provided.
                        if (projectName == "") {
                            selectedProject = ProjectFactory.create(projectType + " " + customer.getLastName(), projectType);
                        } else {
                            selectedProject = ProjectFactory.create(projectName, projectType);
                        }
                        if (selectedProject.getPerson("Customer") == null) {
                            selectedProject.setPerson("Customer", customer);
                        }
                        if (selectedProject.getPerson("Architect") == null) {
                            architect = PersonFactory.create("Contractor");
                            selectedProject.setPerson("Architect", architect);
                        }
                        if (selectedProject.getPerson("Contractor") == null) {
                            contractor = PersonFactory.create("Architect");
                            selectedProject.setPerson("Contractor", contractor);
                        }
                        break;
                    case "quit":
                        continueLoop = false;
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
        System.out.println(project);
        while (true) {
            // Show the project's details and then the menu.
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
                    try {
                        project.markFinalized();
                    } catch(IllegalStateException error) {
                        System.out.println(error.getLocalizedMessage());
                    }
                    if (project.getStatus() != Project.COMPLETION_STATUS.FINALIZED) {
                        System.out.println(project.getInvoice());
                    } else {
                        System.out.println("The project was finalized on " + project.getDateFinalized());
                    }
                    break;
                case "fix missing":
                    ProjectFactory.fixMissingRoles(project);
                    break;
                case "contractor":
                    // Falls through if user enters 'q' in submenu.
                    return (personnelMenuLoop(project.getPerson("contractor")));
                case "quit":
                    return false;
                case "back":
                    return true;
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
                    return true;
                case "quit":
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
}
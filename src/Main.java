
/**
 * An application used to create and modify projects.
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import Components.Database;
import Components.IOController;
import Components.Input;
import Components.ViewStack;
import Components.Menu.Menu;
import Components.Menu.MenuFactory;
import Components.ProjectModel.ProjectNotFoundException;
import Components.ProjectModel;
import Entities.Entity;
import Entities.Person;
import Entities.Project;

public class Main extends IOController {
    private static ViewStack<Menu> menuStack;
    private static ViewStack<Entity> entityStack;
    private static ProjectModel projectModel;

    private Main() {
        super();
    }

    private static void loadMenu() {
        int entities = entityStack.size();
        if (entities == 0) {
            menuStack.push(MAIN_MENU);
        } else if (entityStack.peek() instanceof Person) {
            menuStack.push(PERSONNEL_MENU);
        } else {
            menuStack.push(PROJECT_MENU);
        }
    }

    private static void initializeModel(String[] mainArgs) throws SQLException {
        try {
            InputStream configFileStream;
            if (mainArgs.length == 0) {
                configFileStream = Main.class.getClassLoader().getResourceAsStream(DB_PROPERTIES_FILE);
            } else {
                configFileStream = new FileInputStream(mainArgs[0]);
            }
            projectModel = ProjectModel.getInstance(Database.loadFromFile(configFileStream));
        } catch (IOException ioError) {
            System.out.println("An error occurred while initializing the program.");
            System.out.println(ioError);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        try {
            initializeModel(args);
            projectModel.setRequiredRoles();
        } catch (SQLException initError) {
            System.out.println("An error occurred while initializing the application.");
            System.out.println(initError);
            System.exit(2);
        }
        entityStack = new ViewStack<>();
        menuStack = new ViewStack<>();
        MenuFactory.setStack(menuStack);
        menuStack.push(MAIN_MENU);
        String[] command;
        Menu currentMenu;
        while (true) {
            if (menuStack.empty()) {
                break;
            }
            loadMenu();
            currentMenu = MenuFactory.display();
            try {
                String response = Input.expect("").toString();
                command = response.trim().split("\s");
                if (!currentMenu.containsKey(command[0])) {
                    throw new IllegalArgumentException("This menu does not support that option.");
                }
                switch(command[0]) {
                    case "back":
                        MenuFactory.goBack();
                    case "quit":
                        MenuFactory.quit();
                    default:
                    switch (currentMenu.getName()) {
                        case MAIN_MENU_NAME:
                            mainMenuInteraction(command);
                            break;
                        case PROJECT_MENU_NAME:
                            projectMenuInteraction(command);
                        case PERSON_MENU_NAME:
                            personMenuInteraction(command);
                    }
                }
            } catch (ProjectNotFoundException notFound) {
                System.out.println(notFound.getLocalizedMessage());
            } catch (SQLFeatureNotSupportedException error) {
                error.printStackTrace();
            } catch (SQLException error) {
                error.printStackTrace();
            } catch (NumberFormatException err) {
                System.out.println("A project ID should be a number.");
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
            } catch (IllegalStateException err) {
                System.out.println(err.getLocalizedMessage());
            }
        }
    }

    private static void personMenuInteraction(String[] command) {
    }

    private static void mainMenuInteraction(String... arguments) throws MenuFactory.InvalidSelectionException, SQLException, ProjectNotFoundException {
        Integer projectId = null;
        String option = arguments[0];
        switch (option) {
            case "show":
                if (arguments.length > 1) {
                    showProjects(PROJECT_STATUS.fromString(arguments[1]));
                } else {
                    showProjects(PROJECT_STATUS.ANY);
                }
                break;
            case "select":
                if (arguments.length > 1) {
                    projectId = Integer.parseInt(arguments[1]);
                } else {
                    projectId = Input.expect("Project ID").toInteger();
                }
                Project selectedProject = selectProject(projectId);
                entityStack.push(selectedProject);
                MenuFactory.display();
                break;
            default:
                throw new MenuFactory.InvalidSelectionException(option);
        }
    }

    private static void projectMenuInteraction(String... arguments) throws SQLException, ProjectNotFoundException, MenuFactory.InvalidSelectionException {
        Integer projectId = null;
        String option = arguments[0];
        switch (option) {
            case "due":
                // Menu.goBack();
            case "paid":
                // Menu.quit();
            case "participants":
                if (arguments.length > 1) {
                    showProjects(PROJECT_STATUS.fromString(arguments[1]));
                } else {
                    showProjects(PROJECT_STATUS.ANY);
                }
                break;
            case "fix":
                if (arguments.length == 1) {
                    projectId = Input.expect("Project ID").toInteger();
                } else {
                    projectId = Integer.parseInt(arguments[1]);
                }
                Project selectedProject = selectProject(projectId);
                entityStack.push(selectedProject);
                MenuFactory.display();
                break;
            case "finalize":
                break;
            default:
                throw new MenuFactory.InvalidSelectionException(option);
        }
    }

    private static void showProjects(PROJECT_STATUS status) throws SQLException, ProjectNotFoundException {
        List<Project> projects = projectModel.getProjectList(status);
        for (Project project: projects) {
            System.out.println(project);
        }
    }

    private static Project selectProject(int projectId) throws SQLException, ProjectNotFoundException {
        Project result = projectModel.selectProject(projectId);
        return result;
    }

    private static void showParticipants(int projectId) throws SQLException, ProjectNotFoundException {
        projectModel.listParticipants(entityStack.peek().getNumber());
    }
}
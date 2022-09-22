package Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Components.Input;
import Components.ViewStack;
import Components.Menu.Menu;
import Components.Menu.MenuException;
import Components.Menu.MenuFactory;
import Entities.Entity;
import Entities.EntityDecorator;
import Entities.Person;
import Entities.Project;
import Interfaces.Menus;
import Utilities.EntityUtils;
import Utilities.InputUtils;
import Utilities.OutputUtils;

public class InteractionManager implements Menus {
    private static InteractionManager managerInstance;
    private static MenuFactory menuFactory;
    private static ViewStack<Entity> entityStack;
    private static EntityManager entityManager;

    /**
     * Provides the user with various options to interact with the participants of the selected project.
     * 
     * @throws IOException if an I/O error occurs.
     */
    public void participantMenuInteraction() throws IOException {
        Menu currentMenu = menuFactory.getCurrent();
        String command = currentMenu.getCommand(),
                parameter = currentMenu.getParameter(0);
        HashMap<String, Person> participants = entityManager.getParticipants();
        switch (command) {
            case "list":
                OutputUtils.printMap(participants);
                break;
            case "select":
                String role = InputUtils.getString(parameter, "Project Role");
                entityStack.push(entityManager.selectParticipant(role));
                menuFactory.addMenu(PERSON_MENU);
                break;
        }
    }

    /**
     * Provides the user with various options to interact with a Person.
     * 
     * @throws IOException if an I/O error occurs.
     * @throws SQLException if an error occurs while writing to the database.
     */
    public void personMenuInteraction() throws IOException, SQLException {
        Menu currentMenu = menuFactory.getCurrent();
        String command = currentMenu.getCommand(),
                parameter = currentMenu.getParameter(0);
        Person selectedParticipant = entityManager.getSelectedParticipant();
        switch (command) {
            case "view":
                entityStack.show();
                break;
            case "email":
                selectedParticipant.setPhoneNumber(InputUtils.getString(parameter, "Email Address"));
                break;
            case "phone":
                selectedParticipant.setPhoneNumber(InputUtils.getString(parameter, "Phone Number"));
                break;
        }
        try {
            entityManager.updateContactDetails();
        } catch (SQLException error) {
            System.out.println("An error occurred while updating the database.");
            System.out.println(error);
        }
    }

    /**
     * Creates a new InteractionManager instance for this application if no instance exists, or returns
     * the current instance.
     * 
     * @param _entityManager the application EntityManager.
     * @param _menuFactory the application MenuFactory.
     * @param _entityStack a stack of entities.
     * @return the single InteractionManager instance for this application.
     */
    public static InteractionManager getInstance(EntityManager _entityManager, MenuFactory _menuFactory,
            ViewStack<Entity> _entityStack) {
        if (managerInstance == null) {
            entityStack = _entityStack;
            menuFactory = _menuFactory;
            entityManager = _entityManager;
            managerInstance = new InteractionManager();
        }
        return managerInstance;
    }

    /**
     * The class constructor.
     */
    private InteractionManager() {
    }

    /**
     * Provides the user with various options to interact with the application.
     * 
     * @throws IOException if an I/O error occurs.
     * @throws SQLException if an error occurs while writing to the database.
     */
    public void mainMenuInteraction()
            throws IOException, SQLException {
        Integer projectId = null;
        Menu currentMenu = menuFactory.getCurrent();
        String command = currentMenu.getCommand(),
                parameter = currentMenu.getParameter(0);
        switch (command) {
            case "show":
                PROJECT_VIEW projectView = PROJECT_VIEW.ALL;
                if (parameter != null) {
                    switch (parameter) {
                        case "-i":
                            projectView = PROJECT_VIEW.INCOMPLETE;
                            break;
                        case "-o":
                            projectView = PROJECT_VIEW.OUTSTANDING;
                            break;
                        case "-f":
                            projectView = PROJECT_VIEW.FINALIZED;
                            break;
                    }
                }

                ArrayList<Project> projects = entityManager.getProjectList(projectView);
                if (projects == null) {
                    OutputUtils.printCentered("No projects were found matching the description.");
                } else {
                    for (Project project : projects) {
                        OutputUtils.printDoubleLine();
                        System.out.println(EntityDecorator.listProject(project));
                    }
                }
                break;
            case "select":
                try {
                    projectId = InputUtils.getInteger(parameter, "Project ID");
                    Project project = entityManager.selectProject(projectId);
                    if (project == null) {
                        throw new IllegalStateException("The project could not be loaded.");
                    }
                    entityStack.push(project);
                    menuFactory.addMenu(PROJECT_MENU);
                } catch(NumberFormatException error) {                    
                    OutputUtils.printWarning("The project number should be an integer.");
                } catch(SQLException error) {                    
                    OutputUtils.printWarning("Failed to load the requested project.");
                } catch(IllegalStateException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
                break;
            case "create":
                Person participant = entityManager.getPerson(Input.expect("Customer phone number").toString());
                Person customer = EntityUtils.checkPersonDetails(entityManager, "customer", participant);
                String projectName = Input.query("Project Name").toString();
                int erfNumber = Input.expect("Project ERF Number").toInteger();
                entityManager.findAddress(erfNumber);
                String projectType;
                while (true) {
                    try {
                        projectType = InputUtils.selectFromList(EntityManager.getBuildingTypes());
                        break;
                    } catch (MenuException.InvalidSelectionException e) {
                        System.out.println(e.getLocalizedMessage());
                        continue;
                    }
                }

                int created = entityManager.registerProject(
                        projectName,
                        projectType,
                        erfNumber);
                if (created > 0) {
                    entityManager.registerParticipant("customer", customer.getNumber());
                    OutputUtils.printCentered(String.format(SUCCESS_MESSAGE, "Project"));
                }
                break;
        }
    }

    /**
     * Provides the user with options to view and modify various aspects of the selected project.
     * 
     * @throws IOException if an I/O error occurs.
     * @throws SQLException if an error occurs while writing to the database.
     */
    public void projectMenuInteraction(ViewStack<Entity> entityStack)
            throws SQLException, IOException {
        Menu currentMenu = menuFactory.getCurrent();
        String command = currentMenu.getCommand();
        switch (command) {
            case "view":
                System.out.println(EntityDecorator.showProject((Project) entityStack.peek()));
                break;
            case "account":
                menuFactory.addMenu(ACCOUNT_MENU);
                break;
            case "participants":
                menuFactory.addMenu(PARTICIPANT_MENU);
                break;
            case "fix":
                List<String> missingRoles = entityManager.getMissingRoles();
                if (missingRoles.size() == 0) {
                    OutputUtils.printCentered("No missing roles found.");
                    break;
                }
                assignMissingRoles(missingRoles);
                break;
            case "progress":
                menuFactory.addMenu(PROGRESS_MENU);
                break;

        }
    }

    /**
     * Provides the user with options to update the account (cost, payment) details of the selected project.
     * 
     * @throws IOException if an I/O error occurs.
     * @throws SQLException if an error occurs while writing to the database.
     */
    public void accountMenuInteraction()
            throws IOException, SQLException {
        Menu currentMenu = menuFactory.getCurrent();
        String command = currentMenu.getCommand();
        Project selectedProject = entityManager.getSelectedProject();
        switch (command) {
            case "cost":
                selectedProject.setCost(InputUtils.getDouble("value", "Amount due"));
                break;
            case "paid":
                selectedProject.setPaid(InputUtils.getDouble("value", "Amount paid"));
                break;

        }
        entityManager.setSelectedProject(selectedProject).updateAccount();
    }

    /**
     * Provides options for the user to update the progress(due date, date finalized) of the selected project.
     * 
     * @throws DateTimeParseException if the date provided by the user is invalid.
     * @throws IOException if an I/O error occurs.
     * @throws SQLException if an error occurs while writing to the database.
     */
    public void progressMenuInteraction()
            throws DateTimeParseException, IOException, SQLException {
        Menu currentMenu = menuFactory.getCurrent();
        String command = currentMenu.getCommand();
        Project selectedProject = entityManager.getSelectedProject();
        switch (command) {
            case "due":
                do {
                    selectedProject.setDueDate(InputUtils.getDate(currentMenu.getParameter(0)));
                    break;
                } while (true);
                break;
            case "finalize":
                selectedProject.markFinalized();
                break;
        }
        entityManager.setSelectedProject(selectedProject).updateProgress();
    }

    /**
     * Prompts the user to assign a Person to all in {@code missingRoles}.
     * 
     * @param missingRoles the list of unmapped roles to assign.
     * @throws IOException if an I/O error occurs.
     * @throws SQLException if an error occurs while writing to the database.
     */
    private void assignMissingRoles(List<String> missingRoles) throws IOException, SQLException {
        while (true) {
            System.out.println("Select a role for which to fill in details:");
            String role = InputUtils.selectFromList(missingRoles);
            EntityUtils.checkPersonDetails(entityManager, role, null);
        }
    }
}

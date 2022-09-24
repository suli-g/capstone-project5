package Controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import Components.Input;

import Components.Menu.Menu;
import Entities.Entity;
import Entities.EntityDecorator;
import Entities.Person;
import Entities.Project;
import Interfaces.IMenu;
import Interfaces.IQuery;
import Utilities.EntityUtils;
import Utilities.InputUtils;
import Utilities.OutputUtils;

/**
 * Controls responses by this application to user input.
 */
public class ResponseController implements IQuery, IMenu {
    /**
     * Stores all the allowed participant {@code role} types.
     */
    private static ArrayList<String> roleTypes;

    /**
     * Gets this InteractionContrller instance's {@link #roleTypes}.
     * 
     * @return the list of role types.
     */
    public static ArrayList<String> getRoleTypes() {
        return roleTypes;
    }

    /**
     * Define the list of allowed role types for this application.
     * 
     * @param roleTypes a list of role types to allow.
     */
    public static void setRoleTypes(ArrayList<String> roleTypes) {
        ResponseController.roleTypes = roleTypes;
    }

    /**
     * The controller used in this application to handle interactions.
     */
    private static ResponseController controllerInstance;
    /**
     * The controller used in this application to manage menus.
     */
    private static MenuController menuController;
    /**
     * The stack used in this application to control which menu to display.
     */
    private static Stack<Entity> entityStack;
    /**
     * The controller used in this application to manage menus.
     */
    private static EntityController entityController;
    /**
     * The controller used in this application to manage project participants.
     */
    private static ParticipantController participantController;

    /**
     * Provides the user with various options to interact with the participants of
     * the selected project.
     * 
     * @throws IOException if an I/O error occurs.
     */
    public void participantMenuInteraction() throws IOException {
        Menu currentMenu = menuController.getCurrent();
        String command = currentMenu.getCommand(),
                parameter = currentMenu.getParameter(0);
        HashMap<String, Person> participants = participantController.getParticipants();
        switch (command) {
            case "list":
                OutputUtils.printMap(participants);
                break;
            case "select":
                String role = InputUtils.getString(parameter, "Project Role");
                entityStack.push(participantController.selectParticipant(role));
                menuController.addMenu(PERSON_MENU);
                break;
        }
    }

    /**
     * Provides the user with various options to interact with a Person.
     * 
     * @throws IOException  if an I/O error occurs.
     * @throws SQLException if an error occurs while writing to the database.
     */
    public void personMenuInteraction() throws IOException, SQLException {
        Menu currentMenu = menuController.getCurrent();
        String command = currentMenu.getCommand(),
                parameter = currentMenu.getParameter(0);
        Person selectedParticipant = entityController.getSelectedParticipant();
        switch (command) {
            case "view":
                EntityDecorator.printEntity(entityStack.peek());
                break;
            case "email":
                selectedParticipant.setPhoneNumber(InputUtils.getString(parameter, "Email Address"));
                break;
            case "phone":
                selectedParticipant.setPhoneNumber(InputUtils.getString(parameter, "Phone Number"));
                break;
        }
        try {
            entityController.updateContactDetails();
        } catch (SQLException error) {
            System.out.println("An error occurred while updating the database.");
            System.out.println(error);
        }
    }

    /**
     * Creates a new InteractionManager instance for this application if no instance
     * exists, or returns
     * the current instance.
     * 
     * @param _entityController      the application EntityManager.
     * @param _menuController        the application MenuFactory.
     * @param _participantController a stack of entities.
     * @return the single InteractionManager instance for this application.
     */
    public static ResponseController getInstance(EntityController _entityController, MenuController _menuController,
            ParticipantController _participantController) {
        if (controllerInstance == null) {
            menuController = _menuController;
            entityController = _entityController;
            participantController = _participantController;
            controllerInstance = new ResponseController();
        }
        return controllerInstance;
    }

    /**
     * The class constructor.
     */
    private ResponseController() {
    }

    /**
     * Provides the user with various options to interact with the application.
     * 
     * @throws IOException  if an I/O error occurs.
     * @throws SQLException if an error occurs while writing to the database.
     */
    public void mainMenuInteraction()
            throws IllegalArgumentException, IOException, SQLException {
        Integer projectId = null;
        Menu currentMenu = menuController.getCurrent();
        String command = currentMenu.getCommand(),
                parameter = currentMenu.getParameter(0);
        switch (command) {
            case "show":
                String projectView = PROJECTS_VIEW;
                if (parameter != null) {
                    switch (parameter) {
                        case "-i":
                            projectView = INCOMPLETE_PROJECTS_VIEW;
                            break;
                        case "-o":
                            projectView = OUTSTANDING_PROJECTS_VIEW;
                            break;
                        case "-f":
                            projectView = FINALIZED_PROJECTS_VIEW;
                            break;
                    }
                }
                ResultSet results = entityController.getProjects(projectView);
                if (results == null) {
                    OutputUtils.printCentered("No projects were found matching the description.");
                } else {
                    do {
                        Project currentProject = new Project(
                                results.getInt("project_id"),
                                results.getString("project_name"),
                                results.getString("project_address"),
                                results.getString("project_type"))
                                .setErfNumber(results.getInt("erf_number"))
                                .setCost(results.getInt("amount_due"))
                                .setPaid(results.getInt("amount_paid"))
                                .setDueDate(results.getString("date_due"));
                        String dateFinalized = results.getString("date_finalized");
                        if (dateFinalized == null) {
                            dateFinalized = Project.NOT_SET;
                        } else {
                            currentProject.setDateFinalized(dateFinalized);
                        }
                        EntityDecorator.printEntity(currentProject);
                    } while (results.next());
                }
                break;
            case "select":
                try {
                    projectId = InputUtils.getInteger(parameter, "Project ID");
                    Project project = entityController.getProject(projectId);
                    if (project == null) {
                        throw new IllegalStateException("The project could not be loaded.");
                    }
                    entityStack.push(project);
                    menuController.addMenu(PROJECT_MENU);
                } catch (NumberFormatException error) {
                    OutputUtils.printWarning("The project number should be an integer.");
                } catch (SQLException error) {
                    OutputUtils.printWarning("Failed to load the requested project.");
                } catch (IllegalStateException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
                break;
            case "create":
                String phoneNumber = Input.expect("Customer phone number").toString(PHONE_NUMBER_REGEX);

                Person participant = entityController.findPerson(phoneNumber);
                Person customer = EntityUtils.checkPersonDetails(entityController, "customer", participant);
                String projectName = Input.query("Project Name").toString();
                int erfNumber = Input.expect("Project ERF Number").toInteger();
                entityController.findAddress(erfNumber);
                String projectType;
                while (true) {
                    try {
                        projectType = InputUtils.selectFromList(EntityController.getBuildingTypes());
                        break;
                    } catch (Menu.InvalidSelectionException e) {
                        System.out.println(e.getLocalizedMessage());
                        continue;
                    }
                }

                if (entityController.registerProject(
                        projectName,
                        projectType,
                        erfNumber)) {
                    entityController.registerParticipant("customer", customer.getNumber());
                    OutputUtils.printCentered(String.format(INSERT_SUCCESS_MESSAGE, "Project"));
                } else {
                    OutputUtils.printCentered(String.format(INSERT_FAILURE_MESSAGE, "Project"));
                }
                break;
        }
    }

    /**
     * Provides the user with options to view and modify various aspects of the
     * selected project.
     * 
     * @throws IOException  if an I/O error occurs.
     * @throws SQLException if an error occurs while writing to the database.
     */
    public void projectMenuInteraction()
            throws SQLException, IOException {
        Stack<Entity> entityStack = entityController.getEntityStack();
        Menu currentMenu = menuController.getCurrent();
        String command = currentMenu.getCommand();
        switch (command) {
            case "view":
                EntityDecorator.printEntity(entityStack.peek());
                break;
            case "account":
                menuController.addMenu(ACCOUNT_MENU);
                break;
            case "participants":
                menuController.addMenu(PARTICIPANT_MENU);
                break;
            case "fix":
                List<String> missingRoles = participantController.getMissingRoles();
                if (missingRoles.size() == 0) {
                    OutputUtils.printCentered("No missing roles found.");
                    break;
                }
                assignMissingRoles(missingRoles);
                break;
            case "progress":
                menuController.addMenu(PROGRESS_MENU);
                break;
        }
    }

    /**
     * Provides the user with options to update the account (cost, payment) details
     * of the selected project.
     * 
     * @throws IOException  if an I/O error occurs.
     * @throws SQLException if an error occurs while writing to the database.
     */
    public void accountMenuInteraction()
            throws IOException, SQLException {
        Menu currentMenu = menuController.getCurrent();
        String command = currentMenu.getCommand();
        Project selectedProject = entityController.getSelectedProject();
        switch (command) {
            case "cost":
                selectedProject.setCost(InputUtils.getDouble("value", "Amount due"));
                break;
            case "paid":
                selectedProject.setPaid(InputUtils.getDouble("value", "Amount paid"));
                break;

        }
        if (
            entityController.setSelectedProject(selectedProject).updateAccount()
        ) {

        }
    }

    /**
     * Provides options for the user to update the progress(due date, date
     * finalized) of the selected project.
     * 
     * @throws DateTimeParseException if the date provided by the user is invalid.
     * @throws IOException            if an I/O error occurs.
     * @throws SQLException           if an error occurs while writing to the
     *                                database.
     */
    public void progressMenuInteraction()
            throws DateTimeParseException, IOException, SQLException {
        Menu currentMenu = menuController.getCurrent();
        String command = currentMenu.getCommand();
        Project selectedProject = entityController.getSelectedProject();
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
        entityController.setSelectedProject(selectedProject).updateProgress();
    }

    /**
     * Prompts the user to assign a Person to all in {@code missingRoles}.
     * 
     * @param missingRoles the list of unmapped roles to assign.
     * @throws IOException  if an I/O error occurs.
     * @throws SQLException if an error occurs while writing to the database.
     */
    private void assignMissingRoles(List<String> missingRoles) throws IOException, SQLException {
        while (true) {
            System.out.println("Select a role for which to fill in details:");
            String role = InputUtils.selectFromList(missingRoles);
            EntityUtils.checkPersonDetails(entityController, role, null);
        }
    }
}

package Controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Components.Input;

import Components.Menu.Menu;
import Components.Menu.Menu.InvalidSelectionException;
import Entities.EntityDecorator;
import Entities.Person;
import Entities.Project;
import Interfaces.IMenu;
import Interfaces.IQuery;
import Utilities.InputUtils;
import Utilities.OutputUtils;

/**
 * Controls responses by this application to user input.
 */
public class InputController implements IQuery, IMenu {
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
        InputController.roleTypes = roleTypes;
    }

    /**
     * The controller used in this application to handle interactions.
     */
    private static InputController controllerInstance;
    /**
     * The controller used in this application to manage menus.
     */
    private static MenuController menuController;
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
                entityController.pushToStack(participantController.selectParticipant(role));
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
        Person selectedParticipant = participantController.getSelectedParticipant();
        switch (command) {
            case "view":
                System.out.println(EntityDecorator.decorate(entityController.peekInStack()));
                break;
            case "email":
                selectedParticipant.setPhoneNumber(InputUtils.getString(parameter, "Email Address"));
                break;
            case "phone":
                selectedParticipant.setPhoneNumber(InputUtils.getString(parameter, "Phone Number"));
                break;
        }
        try {
            participantController.updateContactDetails();
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
     * @param _entityController the application EntityManager.
     * @param _menuController   the application MenuFactory.
     * @return the single InteractionManager instance for this application.
     */
    public static InputController getInstance(EntityController _entityController, MenuController _menuController) {
        if (controllerInstance == null) {
            menuController = _menuController;
            entityController = _entityController;
            participantController = entityController.getParticipantController();
            controllerInstance = new InputController();
        }
        return controllerInstance;
    }

    /**
     * The class constructor.
     */
    private InputController() {
    }

    /**
     * Provides the user with various options to interact with the application.
     * 
     * @throws IOException  if an I/O error occurs.
     * @throws SQLException if an error occurs while writing to the database.
     */
    public void mainMenuInteraction()
            throws IllegalArgumentException, IOException, SQLException, InvalidSelectionException {
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
                        System.out.println(EntityDecorator.decorate(currentProject));
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
                    entityController.pushToStack(project);
                    participantController = entityController.getParticipantController();
                    menuController.addMenu(PROJECT_MENU);
                } catch (NumberFormatException error) {
                    OutputUtils.printWarning("The project number should be an integer.");
                } catch (SQLException error) {
                    error.printStackTrace();
                    OutputUtils.printWarning("Failed to load the requested project.");
                } catch (IllegalStateException ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
                break;
            case "create":
                String phoneNumber = Input.expect("Customer phone number")
                        .toString(PHONE_NUMBER_REGEX, PHONE_NUMBER_LIMIT_EXPLANATION);

                Person customer = checkPersonDetails("customer", phoneNumber);
                String projectName = Input.query("Project Name").toString();
                int erfNumber = Input.expect("Project ERF Number").toInteger();
                entityController.findAddress(erfNumber);
                String projectType;
                while (true) {
                        projectType = InputUtils.selectFromList(EntityController.getBuildingTypes());
                        break;
                }

                projectId = entityController.registerProject(
                        projectName,
                        projectType,
                        erfNumber);
                if (projectId == 0) {
                    OutputUtils.printCentered(String.format(INSERT_FAILURE_MESSAGE, "Project"));
                } else {
                    participantController.registerParticipant("customer", customer.getNumber(),
                            projectId);
                    OutputUtils.printCentered(String.format(INSERT_SUCCESS_MESSAGE, "Project"));
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
        Menu currentMenu = menuController.getCurrent();
        String command = currentMenu.getCommand();
        switch (command) {
            case "view":
                System.out.println(EntityDecorator.decorate(entityController.peekInStack()));
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
        String parameter = currentMenu.getParameter(1);
        Project selectedProject = entityController.getSelectedProject();
        switch (command) {
            case "cost":
                selectedProject.setCost(InputUtils.getDouble(parameter, "Amount due"));
                break;
            case "paid":
                selectedProject.setPaid(InputUtils.getDouble(parameter, "Amount paid"));
                break;
        }
        if (!entityController.setSelectedProject(selectedProject).updateAccount()) {
            throw new SQLException(UPDATE_FAILURE_MESSAGE);
        } else {
            OutputUtils.printCentered(UPDATE_SUCCESS_MESSAGE);
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
            checkPersonDetails(role, null);
        }
    }

    /**
     * Verifies that all details of a {@link Person} is already stored in the
     * database and, if not, stores the person's details in the database.
     * 
     * @param role   the role {@code person} should be assigned to if not yet
     *               assigned.
     * @param phoneNumber the phone number of the person to be checked.
     * @return {@code person} supplied to this function.
     * @throws SQLException if a database access error occurs.
     * @throws IOException  if an I/O error occurs.
     */
    public static Person checkPersonDetails(String role, String phoneNumber)
            throws IOException, SQLException {
        Person participant = participantController.findPerson(phoneNumber);
        if (participant == null) {
            OutputUtils.printCentered(new StringBuilder().append("Enter the required details for ")
                    .append(role.toUpperCase()).append(": ").toString());
            int erfNumber = Input.expect("Address ERF Number").toInteger();
            verifyAddress(erfNumber);
            participant = entityController.registerPerson(
                    Input.expect("First name").toString(),
                    Input.expect("Last name").toString(),
                    Input.expect("Email address").toString(),
                    phoneNumber,
                    erfNumber);
        }
        participantController.assignPerson(role, participant);

        return participant;
    }

    /**
     * @param entityController the EntityController instance of this application.
     * @param erfNumber        the ERF number of the address.
     * @return true if the address exists; false if the address does not exit.
     * @throws SQLException if a database access error occurs.
     * @throws IOException  if an I/O error occurs.
     */
    private static boolean verifyAddress(int erfNumber)
            throws SQLException, IOException {
        String address = entityController.findAddress(erfNumber);
        if (address != null) {
            return true;
        }
        System.out.println("Address not found in database. Address Details: ");
        int rowsAffected = entityController.registerAddress(erfNumber,
                Input.expect("Street Address").toString(),
                Input.expect("Suburb").toString(),
                Input.expect("City").toString(),
                Input.expect("Province").toString(),
                Input.expect("Post code").toInteger());
        if (rowsAffected == 0) {
            throw new SQLException(ADDRESS_INSERT_FAILURE_MESSAGE);
        }
        return rowsAffected == 0;
    }
}

package Controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import Entities.Entity;
import Entities.Person;
import Entities.Project;
import Interfaces.IQuery;
import Models.EntityModel.EntityModel;

/**
 * Controls the conversion of database results into {@link Entity}.
 */
public class EntityController implements IQuery {
    /**
     * Stores all the allowed {@code building} types.
     */
    private static ArrayList<String> buildingTypes;
    /**
     * The instance of this EntityController.
     */
    private static EntityController entityControllerInstance;
    /**
     * This application's {@link EntityModel} instance.
     */
    private static EntityModel entityModel;
    /**
     * This EntityModel's {@link ParticipantController} instance.
     */
    private ParticipantController participantController;

    /**
     * @return this EntityController's {@link ParticipantController} instance.
     */
    public ParticipantController getParticipantController() {
        return participantController;
    }

    /**
     * Adds a building type to {@link #buildingTypes}.
     * 
     * @param buildingType the new building type to be added.
     */
    public static void addBuildingType(String buildingType) {
        if (buildingTypes == null) {
            buildingTypes = new ArrayList<>();
        }
        buildingTypes.add(buildingType);
    }

    /**
     * This EntityController's {@link Stack} of {@link Entity} objects.
     */
    private Stack<Entity> entityStack;
    /**
     * The currently selected {@link Project} in this EntityController instance.
     */
    private Project selectedProject;
    /**
     * The currently selected {@code role} in this EntityController instance.
     */


    /**
     * Push a new {@link Entity} to this EntityController instance's
     * {@link #entityStack}.
     * 
     * @param entity the {@link Entity} object to be added.
     * @return this EntityContrller instance.
     */
    public EntityController pushToStack(Entity entity) {
        entityStack.push(entity);
        return this;
    }

    /**
     * Remove the topmost {@link Entity} object from this EntityController
     * instance's {@link #entityStack}.
     * 
     * @return the removed {@link Entity} object.
     */
    public Entity popFromStack() throws EmptyStackException {
        return entityStack.pop();
    }

    /**
     * Peek at the topmost {@link Entity} object from this EntityController
     * instance's {@link #entityStack}.
     * 
     * @return the topmost {@link Entity} object in {@link #entityStack}.
     */
    public Entity peekInStack() throws EmptyStackException {
        return entityStack.peek();
    }

    /**
     * @return the {@link #entityStack}.
     */
    public int getTotalEntitiesLoaded() {
        return entityStack.size();
    }

    /**
     * @return the list of building types.
     */
    public static ArrayList<String> getBuildingTypes() {
        return buildingTypes;
    }

    /**
     * @param index of the building type to get.
     * @return the building type at {@code index} in {@link #buildingTypes}.
     */
    public static String getBuildingType(int index) throws IndexOutOfBoundsException {
        return buildingTypes.get(index);
    }

    /**
     * Sets {@link #selectedProject} to {@code project}.
     * 
     * @param project the project be selected.
     * @return this EntityManager instance.
     */
    public EntityController setSelectedProject(Project project) {
        this.selectedProject = project;
        return this;
    }

    /**
     * Creates a new row in the database with values {@code projectName},
     * {@code projectType}, and {@code erfNumber}.
     * 
     * @param projectName the name of the project.
     * @param projectType the type of the project.
     * @param erfNumber   the ERF number of the project.
     * @return true if the insert was successful, false if not successful.
     * @throws SQLException if an error occurs while writing the data to the
     *                      database.
     */
    public int registerProject(String projectName, String projectType, int erfNumber)
            throws SQLException {
        PreparedStatement registration = entityModel.registerProject(projectName, projectType, erfNumber);
        int affectedRows = registration.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException(INSERT_FAILURE_MESSAGE);
        }
        try (ResultSet generatedKeys = registration.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException(INSERT_FAILURE_MESSAGE);
            }
        }
    }

    /**
     * @param erfNumber the ERF number to look for in the table of addresses.
     * @return The address linked to {@code erfNumber} if the address is found, null
     *         if not found.
     * @throws SQLException if an error occurs while accessing the database.
     */
    public String findAddress(int erfNumber) throws SQLException {
        ResultSet results = entityModel.getAddress(erfNumber);
        if (results != null) {
            return results.getString("full_address");
        }
        return null;
    }

    /**
     * Inserts a new address, with the provided details, into the database.
     * 
     * @param erfNumber     the address ERF number.
     * @param streetAddress the street address.
     * @param suburb        the suburb.
     * @param city          the city.
     * @param province      the province.
     * @param postCode      the post code.
     * @return true if the registration is successful, false if
     *         not.
     * @throws SQLException if a database error occurs.
     */
    public int registerAddress(
            int erfNumber, String streetAddress, String suburb,
            String city, String province, int postCode) throws SQLException {
        return entityModel.registerAddress(erfNumber, streetAddress, suburb, city, province,
                postCode).executeUpdate();
    }

    /**
     * @return the currently selected {@link Project}.
     */
    public Project getSelectedProject() {
        return selectedProject;
    }

    /**
     * Inserts the given parameters into the 'person' table in the database.
     * 
     * @param firstName    the first name of the person being registered
     * @param lastName     the last name of the person being registered
     * @param emailAddress the email address of the person being registered
     * @param phoneNumber  the phone number of the person being registered
     * @param erfNumber    the erf number of the address of the person being
     *                     registered
     * @return a new {@link Person} object with the given properties.
     * @throws SQLException if a database error occurs.
     */
    public Person registerPerson(String firstName, String lastName, String emailAddress, String phoneNumber,
            int erfNumber)
            throws SQLException {
        entityModel.registerPerson(firstName, lastName, emailAddress, phoneNumber, erfNumber);
        Person person = participantController.findPerson(phoneNumber);
        return person;
    }

    /**
     * Queries the database {@code table} for all projects.
     * 
     * @param table the table from which to query the project data.
     * @return a {@link ResultSet} if no projects exist.
     * @throws SQLException if a database error occurs
     */
    public ResultSet getProjects(String table) throws SQLException {
        ResultSet results = entityModel.getProjects(table);
        if (results == null) {
            return null;
        }
        return results;
    }

    /**
     * @param projectId the project_id of the project in the database.
     * @return the selected project if the project exists, null if the project does
     *         not exist.
     * @throws SQLException if a database error occurs.
     */
    public Project getProject(int projectId) throws SQLException {
        ResultSet results = entityModel.selectProject(projectId);
        if (results == null) {
            return null;
        }
        selectedProject = new Project(
                results.getInt("project_id"),
                results.getString("project_name"),
                results.getString("project_address"),
                results.getString("project_type"))
                .setDueDate(results.getString("date_due"))
                .setDateFinalized(results.getString("date_finalized"))
                .setErfNumber(results.getInt("erf_number"))
                .setCost((double) results.getInt("amount_due"))
                .setPaid(results.getInt("amount_paid"));
        participantController = ParticipantController.getInstance(entityModel);
        participantController.setParticipants(entityModel.getParticipants(selectedProject.getNumber()));
        return selectedProject;
    }

    /**
     * @return 1 if the update is successful, 0 if not successful.
     * @throws SQLException if a database error occurs.
     */
    public boolean updateProgress() throws SQLException {
        return entityModel.updateProgress(selectedProject.getNumber(), selectedProject.getDueDate(),
                selectedProject.getDateFinalized());
    }



    /**
     * @return true if the update was successful, false if the update failed
     * @throws SQLException if a database error occurs.
     */
    public boolean updateAccount() throws SQLException {
        int projectId = selectedProject.getNumber(),
                amountDue = (int) selectedProject.getCost() * 100,
                amountPaid = (int) selectedProject.getPaid() * 100;
        return entityModel.updateAccount(projectId, amountDue, amountPaid);
    }

    /**
     * @return the last id inserted into the database.
     * @throws SQLException if a database communication error occurs.
     */
    public int getLastInsertId() throws SQLException {
        return entityModel.getLastInsertId();
    }

    /**
     * @param model the model to use for queries
     * @return this EntityManager instance
     * @throws SQLException if a database error occurs.
     */
    public static EntityController getInstance(EntityModel model) throws SQLException {
        if (entityControllerInstance == null) {
            entityModel = model;            
            loadTypes();
            entityModel = model;
            entityControllerInstance = new EntityController();
        }
        return entityControllerInstance;
    }

    /**
     * Loads all building and relationship types to the application from their respective tables.
     * 
     * @throws SQLException if a database communication error occurs.
     */
    private static void loadTypes() throws SQLException{
        ResultSet results = entityModel.loadTypes();
        if (results == null) {
            throw new SQLException(NO_TYPES);
        }
        do {
            String buildingType = results.getString("building_type");
            String roleType = results.getString("relationship_type");
            if (buildingType != null) {
                addBuildingType(buildingType);
            }
            if (roleType != null) {
                ParticipantController.addRoleType(roleType);
            }
        } while (results.next());
    }

    private EntityController() {
        entityStack = new Stack<>();
    }
}

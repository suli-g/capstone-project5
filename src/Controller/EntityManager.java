package Controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Entities.Person;
import Entities.Project;
import Interfaces.Config;
import Model.EntityModel.EntityModel;

public class EntityManager implements Config {
    private static ArrayList<String> roleTypes;
    private static ArrayList<String> buildingTypes;
    private static EntityManager managerInstance;
    private static EntityModel projectModel;
    private Project selectedProject;
    private String selectedRole;

    /**
     * @return the {@link Person} mapped to the {@code selectedRole} key.
     */
    public Person getSelectedParticipant() {
        return participants.get(selectedRole);
    }

    /**
     * Inserts {@code person} at the {@code selectedRole} key in
     * {@link #participants}.
     * 
     * @param person the person to insert.
     * @return this {@code EntityManager} instance.
     */
    public EntityManager updateSelectedPerson(Person person) {
        participants.put(selectedRole, person);
        return this;
    }

    /**
     * Sets {@link #selectedRole} to {@code role}.
     * 
     * @param role the role to be selected.
     * @return this {@code EntityManager} instance.
     */
    public EntityManager setSelectedRole(String role) {
        this.selectedRole = role;
        return this;
    }

    private HashMap<String, Person> participants;

    /**
     * @return the list of role types.
     */
    public static ArrayList<String> getRoleTypes() {
        return roleTypes;
    }

    /**
     * @param index the index to access.
     * @return the {@code role} at {@code index} in the list of role types.
     * @throws IndexOutOfBoundsException if {@code index >} the size of
     *                                   {@link #roleTypes}
     */
    public static String getRoleType(int index) {
        return roleTypes.get(index);
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
    public EntityManager setSelectedProject(Project project) {
        this.selectedProject = project;
        return this;
    }

    /**
     * Gets the list of participants from {@link #projectModel} and stores it in
     * {@link #participants}.
     * 
     * @throws SQLException if a Database communication occurs.
     */
    public void setParticipants() throws SQLException {
        ResultSet results = projectModel.getParticipants(selectedProject.getNumber());
        if (participants == null) {
            participants = new HashMap<>();
        }
        if (results == null) {
            return;
        }
        do {
            Person participant = new Person(results.getInt("person_id"),
                    results.getString("first_name"),
                    results.getString("last_name"),
                    results.getString("physical_address"),
                    results.getString("email_address"))
                    .setPhoneNumber(results.getString("phone_number"));
            assignPerson(results.getString("relationship_type"), participant);
        } while (results.next());
    }

    /**
     * Creates a new row in the database with values {@code projectName},
     * {@code projectType}, and {@code erfNumber}.
     * 
     * @param projectName the name of the project.
     * @param projectType the type of the project.
     * @param erfNumber   the ERF number of the project.
     * @return {@value 1} if the database operation is successful, {@value 0} if
     *         not.
     * @throws SQLException if an error occurs while writing the data to the
     *                      database.
     */
    public int registerProject(String projectName, String projectType, int erfNumber)
            throws SQLException {
        return projectModel.registerProject(projectName, projectType, erfNumber);
    }

    /**
     * @param erfNumber the ERF number to look for in the table of addresses.
     * @return The address linked to {@code erfNumber} if the address is found, null
     *         if not found.
     * @throws SQLException if an error occurs while accessing the database.
     */
    public String findAddress(int erfNumber) throws SQLException {
        ResultSet results = projectModel.getAddress(erfNumber);
        if (results != null) {
            return results.getString("full_address");
        }
        return null;
    }

    /**
     * @param erfNumber
     * @param streetAddress
     * @param suburb
     * @param city
     * @param province
     * @param postCode
     * @return boolean
     * @throws SQLException
     */
    public boolean registerAddress(
            int erfNumber, String streetAddress, String suburb,
            String city, String province, int postCode) throws SQLException {
        boolean registeredSuccessfully = projectModel.registerAddress(erfNumber, streetAddress, suburb, city, province,
                postCode) > 0;
        if (registeredSuccessfully) {

            return true;
        }
        return false;
    }

    /**
     * @param role
     * @param person
     */
    public void assignPerson(String role, Person person) {
        participants.put(role, person);
    }

    /**
     * @return Project
     */
    public Project getSelectedProject() {
        return selectedProject;
    }

    /**
     * @param firstName
     * @param lastName
     * @param emailAddress
     * @param phoneNumber
     * @param erfNumber
     * @return Person
     * @throws SQLException
     */
    public Person registerPerson(String firstName, String lastName, String emailAddress, String phoneNumber,
            int erfNumber)
            throws SQLException {
        projectModel.registerPerson(firstName, lastName, emailAddress, phoneNumber, erfNumber);
        Person person = getPerson(phoneNumber);
        return person;
    }

    /**
     * @param role
     * @param personId
     * @return Person
     * @throws SQLException
     */
    public Person registerParticipant(String role, int personId)
            throws SQLException {
        projectModel.registerParticipant(selectedProject.getNumber(), personId, role);
        Person person = getPerson(personId);
        assignPerson(role, person);
        return person;
    }

    /**
     * @param phoneNumber
     * @return Person
     * @throws SQLException
     * @throws NumberFormatException
     */
    public Person getPerson(String phoneNumber)
            throws SQLException, NumberFormatException {
        ResultSet results = projectModel.selectPerson(phoneNumber);
        if (results == null) {
            return null;
        } else {
            return new Person(results.getInt("person_id"),
                    results.getString("first_name"),
                    results.getString("last_name"),
                    results.getString("physical_address"),
                    results.getString("email_address"))
                    .setPhoneNumber(results.getString("phone_number"));
        }
    }

    /**
     * @param personId
     * @return Person
     * @throws SQLException
     * @throws NumberFormatException
     */
    public Person getPerson(int personId)
            throws SQLException, NumberFormatException {
        ResultSet results = projectModel.selectPerson(personId);
        if (results == null) {
            return null;
        } else {
            return new Person(results.getInt("person_id"),
                    results.getString("first_name"),
                    results.getString("last_name"),
                    results.getString("physical_address"),
                    results.getString("email_address"))
                    .setPhoneNumber(results.getString("phone_number"));
        }
    }

    /**
     * @return HashMap<String, Person>
     */
    public HashMap<String, Person> getParticipants() {
        return participants;
    }

    /**
     * @return List<String>
     */
    public List<String> getMissingRoles() {
        List<String> missingRoles = roleTypes;
        roleTypes.removeIf(role -> participants.containsKey(role));
        return missingRoles;
    }

    /**
     * @param role
     * @return Person
     */
    public Person getParticipant(String role) {
        return participants.get(role);
    }

    /**
     * @param view
     * @return ArrayList<Project>
     * @throws SQLException
     */
    public ArrayList<Project> getProjectList(PROJECT_VIEW view) throws SQLException {
        ResultSet results = projectModel.getProjects(view);
        ArrayList<Project> projects = new ArrayList<>();
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
                dateFinalized = "n/a";
            } else {
                currentProject.setDateFinalized(dateFinalized);
            }
            projects.add(
                    currentProject);
        } while (results.next());
        return projects;
    }

    /**
     * @param role
     * @return Person
     */
    public Person selectParticipant(String role) {
        selectedRole = role;
        return participants.get(role);
    }

    /**
     * @param projectId
     * @return Project
     * @throws SQLException
     */
    public Project selectProject(int projectId) throws SQLException {
        ResultSet results = projectModel.selectProject(projectId);
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
        setParticipants();
        return selectedProject;
    }

    /**
     * @return int
     * @throws SQLException
     */
    public int updateProgress() throws SQLException {
        return projectModel.updateProgress(selectedProject.getNumber(), selectedProject.getDueDate(),
                selectedProject.getDateFinalized());
    }

    /**
     * @return int
     * @throws SQLException
     */
    public int updateContactDetails() throws SQLException {
        Person selectedPerson = getSelectedParticipant();
        int personId = selectedPerson.getNumber();
        String phoneNumber = selectedPerson.getPhoneNumber(),
                emailAddress = selectedPerson.getEmailAddress();
        return projectModel.updateContactDetails(personId, phoneNumber, emailAddress);
    }

    /**
     * @return int
     * @throws SQLException
     */
    public int updateAccount() throws SQLException {
        int projectId = selectedProject.getNumber(),
                amountDue = (int) selectedProject.getCost() * 100,
                amountPaid = (int) selectedProject.getPaid() * 100;
        return projectModel.updateAccount(projectId, amountDue, amountPaid);
    }

    /**
     * @param role
     * @return Person
     */
    public Person selectPerson(String role) {
        return participants.get(role);
    }

    /**
     * @param model
     * @return EntityManager
     * @throws SQLException
     */
    public static EntityManager getInstance(EntityModel model) throws SQLException {
        if (managerInstance == null) {
            projectModel = model;

            ResultSet results;
            do {
                results = model.loadTypes();
                if (results == null) {
                    throw new SQLException();
                }
            } while (results == null);
            buildingTypes = new ArrayList<>();
            roleTypes = new ArrayList<>();
            do {
                String buildingType = results.getString("building_type");
                String roleType = results.getString("relationship_type");
                if (buildingType != null) {
                    buildingTypes.add(buildingType);
                }
                if (roleType != null) {
                    roleTypes.add(roleType);
                }
            } while (results.next());
            projectModel = model;
            managerInstance = new EntityManager();
        }
        return managerInstance;
    }

    private EntityManager() {
    }
}

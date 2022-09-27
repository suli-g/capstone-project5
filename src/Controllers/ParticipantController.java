package Controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Entities.Person;
import Factories.PersonFactory;
import Models.EntityModel.EntityModel;

/**
 * Controls interactions related to participants in a project.
 */
public class ParticipantController {
    private HashMap<String, Person> participants;
    private static ArrayList<String> roleTypes;

    /**
     * Adds a role type to the list of role types ({@link roleTypes}).
     * 
     * @param roleType The role type to add.
     */
    public static void addRoleType(String roleType) {
        if (roleTypes == null) {
            roleTypes = new ArrayList<>();
        }
        roleTypes.add(roleType);
    }

    /**
     * The available instance of ParticipantController in this application.
     */
    private static ParticipantController controllerInstance;

    /**
     * This class's constructor.
     */
    private ParticipantController() {
        participants = new HashMap<>();
    }

    /**
     * @param role the role of the participant to get.
     * @return the {@link Person} with the given {@code role} if found; otherwise
     *         {@code null}.
     */
    public Person getParticipant(String role) {
        if (participants == null) {
            participants = new HashMap<>();
            return null;
        }
        return participants.get(role);
    }

    /**
     * @return this ParticipantController instance's {@link #participants} map.
     */
    public HashMap<String, Person> getParticipants() {
        if (participants == null) {
            participants = new HashMap<>();
        }
        return participants;
    }

    /**
     * Checks if this ParticipantController instance has a participant map and has
     * any mapped participants.
     * 
     * @return {@code true} if the participant map exists or has any participants;
     *         {@code false} otherwise.
     */
    public boolean hasParticipants() {
        return participants == null || participants.size() == 0;
    }

    private static EntityModel participantModel;

    /**
     * Creates and retrieves a new instance of ParticipantController.
     * 
     * @param model the model to be used for communicating with the database.
     * @return the new ParticipantController instance.
     */
    public static ParticipantController getInstance(EntityModel model) {
        if (controllerInstance == null) {
            participantModel = model;
        }
        controllerInstance = new ParticipantController();
        return controllerInstance;
    }

    /**
     * @param role the role to be assigned.
     * @return the {@link Person} assigned to {@code role}
     */
    public Person selectParticipant(String role) {
        selectedRole = role;
        return participants.get(role);
    }

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
     *                                   {@link #roleTypes}.
     */
    public static String getRoleType(int index) {
        return roleTypes.get(index);
    }

    /**
     * Sets {@link #selectedRole} to {@code role}.
     * 
     * @param role the role to be selected.
     * @return this {@code EntityManager} instance.
     */
    public ParticipantController setSelectedRole(String role) {
        this.selectedRole = role;
        return this;
    }

    /**
     * Inserts {@code person} at the {@code selectedRole} key in
     * {@link #participants}.
     * 
     * @param person the person to insert.
     * @return this {@code EntityManager} instance.
     */
    public ParticipantController updateSelectedParticipant(Person person) {
        participants.put(selectedRole, person);
        return this;
    }

    /**
     * Assigns a {@link Person} to a role in the currently selected project.
     * 
     * @param role   the role to which {@code person} should be assigned.
     * @param person the person to be assigned.
     */
    public void assignPerson(String role, Person person) {
        participants.put(role, person);
    }

    /**
     * Gets the list of participants from {@code results} and stores it as a
     * {@link HashMap}.
     * 
     * @param results a set of results containing the details for all participants.
     * @throws SQLException if a Database communication occurs.
     */
    public void setParticipants(ResultSet results) throws SQLException {
        if (participants == null) {
            participants = new HashMap<>();
        } if (results == null) {
            throw new IllegalAccessError("The selected project has no participants.");
        }
        do {
            assignPerson(results.getString("relationship_type"), PersonFactory.fromResultSet(results));
        } while (results.next());
    }

    /**
     * Finds all {@code roles} in {@link #roleTypes} which are not in
     * {@link #participants}.
     * 
     * @return {@link #roleTypes} with the {@link #participants} keys removed.
     */
    public List<String> getMissingRoles() {
        List<String> missingRoles = roleTypes;
        roleTypes.removeIf(role -> participants.containsKey(role));
        return missingRoles;
    }

    private String selectedRole;

    /**
     * @return the {@link Person} mapped to the {@code selectedRole} key.
     */
    public Person getSelectedParticipant() {
        return participants.get(selectedRole);
    }

    /**
     * Inserts the given parameters into the 'participant' table in the database.
     * 
     * @param role the person should be assigned to.
     * @param personId of the person to be assigned.
     * @param projectId of the project to which the {@code personId} should be assigned..
     * @return the result of {@link #getPerson(personId)}
     * @throws SQLException if a database error occurs
     */
    public Person registerParticipant(String role, int personId, int projectId)
            throws SQLException {
        participantModel.registerParticipant(projectId, personId, role).executeUpdate();
        Person person = getPerson(personId);
        assignPerson(role, person);
        return person;
    }

    /**
     * Finds the row in the 'person' table with the given 'phone_number' value.
     * 
     * @param phoneNumber the phone_number of the person to find.
     * @return a new {@link Person} object if a match is found for the phone_number.
     * @throws SQLException if a database error occurs.
     */
    public Person findPerson(String phoneNumber)
            throws SQLException {
        ResultSet results = participantModel.selectPerson(phoneNumber);
        if (results == null) {
            return null;
        }
        return PersonFactory.fromResultSet(results);
    }

    /**
     * Finds the row in the 'person' table with the given 'person_id' value.
     * 
     * @param personId the value of the 'person_id' column in the database.
     * @return a new {@link Person} if the row is found, null if not.
     * @throws SQLException if a database error occurs.
     */
    public Person getPerson(int personId)
            throws SQLException {
        ResultSet results = participantModel.selectPerson(personId);
        if (results == null) {
            return null;
        } else {
            return PersonFactory.fromResultSet(results);
        }
    }

    /**
     * @return true if the update was successful, false if the update failed.
     * @throws SQLException if a database error occurs.
     */
    public boolean updateContactDetails() throws SQLException {
        Person selectedPerson = getSelectedParticipant();
        int personId = selectedPerson.getNumber();
        String phoneNumber = selectedPerson.getPhoneNumber(),
                emailAddress = selectedPerson.getEmailAddress();
        return participantModel.updateContactDetails(personId, phoneNumber, emailAddress);
    }
}

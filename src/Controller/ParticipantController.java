package Controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Entities.Person;

/**
 * Controls interactions related to participants in a project.
 */
public class ParticipantController {
    private HashMap<String, Person> participants;
    private static ArrayList<String> roleTypes;

    /**
     * Sets the list of allowe role types for this ParticipantController instance.
     * 
     * @param roleTypes the list of role types.
     */
    public static void setRoleTypes(ArrayList<String> roleTypes) {
        ParticipantController.roleTypes = roleTypes;
    }

    /**
     * The selected role.
     */
    private String selectedRole;
    /**
     * The available instance of ParticipantController in this application.
     */
    private static ParticipantController controllerInstance;

    /**
     * This class's constructor.
     */
    private ParticipantController() {
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

    /**
     * Creates and retrieves a new instance of ParticipantController.
     * 
     * @return the new ParticipantController instance.
     */
    public static ParticipantController getNewInstance() {
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
        if (results == null) {
            participants = new HashMap<>();
        }
        do {
            Person participant = ParticipantController.parsePerson(results.getInt("person_id"),
                    results.getString("first_name"),
                    results.getString("last_name"),
                    results.getString("physical_address"),
                    results.getString("email_address"), results.getString("phone_number"));
            assignPerson(results.getString("relationship_type"), participant);
        } while (results.next());
    }

    /**
     * Creates a new {@link Person} object with the given parameters.
     * 
     * @param personId     of the Person
     * @param firstName    of the Person
     * @param lastName     of the Person
     * @param emailAddress of the Person
     * @param phoneNumber  of the Person
     * @param physicalAddress  of the Person
     * @return a {@link Person} object with the given parameters.
     */
    public static Person parsePerson(int personId, String firstName, String lastName, String emailAddress,
            String phoneNumber, String physicalAddress) {
        return new Person(personId, firstName, lastName, physicalAddress, emailAddress);
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
}

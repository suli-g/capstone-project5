package Entities;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;

import config.Overviews;

/**
 * Represents a project
 */
public class Project extends Entity implements Overviews {
    private static List<String> requiredRoles;
    private HashMap<String, Person> participants;
    private double cost = 0,
            paid = 0;

    public Project setCost(double cost) {
        this.cost = cost;
        return this;
    }

    public Project setCost(int cost) {
        this.cost = cost / 100;
        return this;
    }

    private String dueDate,
            dateFinalized = null;
    private int erfNumber;

    /**
     * Project constructor.
     * 
     * @param projectName    the name of this project.
     * @param projectAddress the address of this project.
     * @param buildingType   the type of building this project represents.
     * @param erfNumber      the ERF number of this project.
     * @param projectCost    the cost of this project.
     */
    public Project(int projectNumber, String projectName, String projectAddress, String buildingType) {
        super(projectName, projectAddress, buildingType, projectNumber);
        participants = new HashMap<>();
    }

    /**
     * @param role the role of the {@link Entities.Person} to retrieve.
     * @return the {@link Entities.Person} associated with role, or null.
     * @throws IllegalArgumentException if the role is not in
     *                                  {@link #requiredRoles}.
     */
    public Person getPerson(String role) {
        if (!requiredRoles.contains(role)) {
            throw new IllegalArgumentException("The role '" + role + "' is invalid for this project.");
        }
        return participants.get(role);
    }

    /**
     * @return A list of roles not yet assigned for this project.
     */
    public List<String> getMissingRoles() {
        return requiredRoles.stream().filter(role -> participants.get(role) == null).toList();
    }

    /**
     * @param role   the role to associate with {@code person}.
     * @param person the person to associate with {@code role}, or null to add
     *               {@code role} to {@link #requiredRoles}.
     * @return this Project.
     * @throws IllegalArgumentException if {@code role} is not in
     *                                  {@link #requiredRoles}.
     */
    public Project setPerson(String role, Person person) throws IllegalArgumentException {
        if (!requiredRoles.contains(role)) {
            throw new IllegalArgumentException("The role '" + role + "' is not a valid project role.");
        }
        participants.put(role, person);
        return this;
    }

    /**
     * @return a list of required {@code roles}.
     */
    public static List<String> getRequiredRoles() {
        return requiredRoles;
    }

    /**
     * Defines which roles are legal to associate with this Project object.
     * 
     * @param roles the roles to be associated.
     */
    public static void setRequiredRoles(List<String> roles) {
        requiredRoles = roles;
    }

    /**
     * Sets this Project's due date.
     * 
     * @param date the due date as a string.
     * @return this Project
     * @throws DateTimeParseException if {@code date} does not represent a valid
     *                                date.
     */
    public Project setDueDate(String date) throws DateTimeParseException {
        // First validate the details of the new date before assigning it.
        if (LocalDate.parse(date).isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("That date has already expired!");
        } else {
            dueDate = date;
        }
        return this;
    }

    /**
     * Sets the total amount paid for this project.
     * 
     * @param amount the amount paid in rands.
     * @return this project.
     */
    public Project setPaid(double amount) {
        paid = amount;
        return this;
    }

    public Project setPaid(int amount) {
        paid = amount / 100;
        return this;
    }

    /**
     * @return the amount paid.
     */
    public double getPaid() {
        return paid;
    }

    /**
     * @return the total cost of this project.
     */
    public double getCost() {
        return cost;
    }

    /**
     * @return the due date of this project.
     */
    public String getDueDate() {
        return dueDate.toString();
    }

    /**
     * @return the date this project was finalized.
     */
    public String getDateFinalized() {
        return dateFinalized != null ? dateFinalized : "n/a";
    }

    /**
     * Marks the project as finalized if all costs have been paid.
     * 
     * @return the finalization status of the project.
     */
    public Project markFinalized() {
        return this.markFinalized(LocalDate.now().toString());
    }

    public int getErfNumber() {
        return erfNumber;
    }

    public Project setErfNumber(int erfNumber) {
        this.erfNumber = erfNumber;
        return this;
    }

    /**
     * Marks the project as finalized on {@code date} if all details are in order.
     * 
     * @param finalizationDate the date this project was finalized on.
     * @return the finalization status of the project.
     * @throws DateTimeParseException if {@code finalizationDate} is not a valid
     *                                date string.
     * @throws IllegalStateException  If this project connot be finalized (details
     *                                missing).
     */
    public Project markFinalized(String finalizationDate) throws DateTimeParseException, IllegalStateException {
        List<String> missingRoles = getMissingRoles();
        if (missingRoles.size() == 0 && cost <= paid) {
            if (finalizationDate != "-") {
                this.dateFinalized = LocalDate.now().toString();
            }
            this.dateFinalized = LocalDate.parse(finalizationDate).toString();
        } else if (cost > paid) {
            throw new IllegalStateException("There are still outstanding costs.");
        } else {
            throw new IllegalStateException(
                    "The project is still missing participants: " + missingRoles.toString());
        }
        return this;
    }

    /**
     * Stores this Project's details in a logical format.
     */
    @Override
    public String toString() {

        return new StringBuilder()
                .append(String.format(
                        PROJECT_LIST_FORMAT, number, getName(), address, getType(), getErfNumber(),
                        getCost(), getPaid(), getDueDate(), getDateFinalized()))
                .append("-")
                .toString();
    }
}
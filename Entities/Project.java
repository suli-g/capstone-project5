package Entities;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a project
 */
public class Project extends Entity {
    /**
     * Represents the completion status of this Project instance.
     */
    public enum COMPLETION_STATUS {
        IN_PROGRESS("IN PROGRESS"),
        FINALIZED("FINALIZED"),
        OVERDUE("OVERDUE");

        public final String label;

        private COMPLETION_STATUS(String label) {
            this.label = label;
        }
    }

    private static List<String> requiredRoles;
    private ArrayList<String> missingRoles;
    private HashMap<String, Person> participants;
    private double cost = 0,
            paid = 0;
    private String dueDate,
            dateFinalized = null;

    /**
     * Project constructor.
     * 
     * @param projectName the name of this project.
     * @param projectAddress the address of this project.
     * @param buildingType the type of building this project represents.
     * @param erfNumber the ERF number of this project.
     * @param projectCost the cost of this project.
     */
    public Project(String projectName, String projectAddress, String buildingType, int erfNumber, double projectCost) {
        super(projectName, projectAddress, buildingType, erfNumber);
        cost = projectCost;
        participants = new HashMap<>();
        this.missingRoles = new ArrayList<>() {
            {
                for (String role : getRequiredRoles()) {
                    add(role);
                }
            }
        };
    }

    /**
     * @param role the role of the {@link Entities.Person} to retrieve.
     * @return the {@link Entities.Person} associated with role, or null.
     * @throws IllegalArgumentException if the role is not in {@link #requiredRoles}.
     */
    public Person getPerson(String role) {
        if (!requiredRoles.contains(role)) {
            throw new IllegalArgumentException("The role '" + role + "' is invalid for this project.");
        }
        return participants.get(role);
    }

    /**
     * @param role the role to associate with {@code person}.
     * @param person the person to associate with {@code role}, or null to add {@code role} to {@link #requiredRoles}.
     * @return this Project.
     * @throws IllegalArgumentException if {@code role} is not in {@link #requiredRoles}.
     */
    public Project set(String role, Person person) throws IllegalArgumentException {
        if (!requiredRoles.contains(role)) {
            throw new IllegalArgumentException("The role '" + role + "' is not a valid project role.");
        } if (person == null) {
            missingRoles.add(role);
        }
        missingRoles.remove(role);
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
    public static void setRequiredRoles(String... roles) {
        requiredRoles = Arrays.asList(roles);
    }

    /**
     * Sets the project due date.
     * 
     * @param year       the year at which this project is due.
     * @param month      the month at which this project is due.
     * @param dayOfMonth the day of the month at which this project is due.
     * @return this project.
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
        return dateFinalized;
    }

    /**
     * Marks the project as finalized if all costs have been paid.
     * 
     * @return the finalization status of the project.
     */
    public Project markFinalized() {
        return this.markFinalized(LocalDate.now().toString());
    }

    /**
     * Marks the project as finalized on the given date.
     * 
     * @return the finalization status of the project.
     */
    public Project markFinalized(String finalizationDate) throws DateTimeParseException {
        if (missingRoles.size() == 0 && cost == paid) {
            if (finalizationDate == "-") {
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
     * { @return project finalization status}
     */
    public COMPLETION_STATUS getStatus() {
        if (dateFinalized != null) {
            return COMPLETION_STATUS.FINALIZED;
        } else if (dueDate != null && LocalDate.parse(dueDate).isBefore(LocalDate.now())) {
            return COMPLETION_STATUS.OVERDUE;
        }
        return COMPLETION_STATUS.IN_PROGRESS;
    }

    /**
     * Gets an invoice for this project containing the customer and account details.
     * 
     * @return the project invoice.
     */
    public String getInvoice() {
        StringBuilder invoiceBuilder = new StringBuilder();
        Person participant;
        for (String role : requiredRoles) {
            participant = getPerson(role);
            invoiceBuilder
                    .append("[")
                    .append(role)
                    .append(" Details]\n")
                    .append(participant == null ? "N/A" : participant)
                    .append('\n');
        }
        invoiceBuilder.append("[Account Details]\n")
                .append(String.format("""
                        Total Cost:            R%.2f
                        Total Paid:            R%.2f
                        Outstanding Balance:   R%.2f\n
                        """, cost, paid, cost - paid));
        return invoiceBuilder
                .toString();
    }

    /**
     * Stores this Project's details in a logical format.
     */
    @Override
    public String toString() {
        String projectDetails = String.format("""
                -
                [PROJECT DETAILS]
                Project Name:       %s
                Project Address:    %s
                Project Type:       %s
                ERF Number:         %d
                """, getName(), address, getType(), getNumber());
        return new StringBuilder()
                .append(projectDetails)
                .append(getInvoice())
                .append("-")
                .toString();
    }
}
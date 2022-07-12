package Entities;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import Enums.COMPLETION_STATUS;

/**
 * Represents a project
 */
public class Project extends Entity {
    // The format to use for all project-related date strings.
    private static List<String> requiredParticipants;
    private ArrayList<String> missingParticipants;
    public static List<String> getRequiredParticipants() {
        return requiredParticipants;
    }

    private HashMap<String, Person> participants;
    private double cost = 0,
            paid = 0;
    private String dueDate,
            dateFinalized = null;
    /**
     * Project constructor.
     * 
     * @param projectName
     * @param projectAddress
     * @param projectType
     * @param erfNumber
     * @param projectCost
     */
    public Project(String projectName, String projectAddress, String projectType, int erfNumber, double projectCost) {
        super(projectName, projectAddress, projectType, erfNumber);
        cost = projectCost;
        participants = new HashMap<>();
        this.missingParticipants = new ArrayList<>() {{
            for (String role: getRequiredParticipants()) {
                add(role);
            }
        }};
    }


    public Person get(String role) {
        return participants.get(role);
    }

    public Project set(String role, Person person) throws IllegalArgumentException {
        if (!requiredParticipants.contains(role)) {
            throw new IllegalArgumentException("The role '"+ role +"' is not a valid project role.");
        }
        participants.put(role, person);
        return this;
    }

    public static void setRequiredParticipants(String...positions) {
        requiredParticipants = Arrays.asList(positions);
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
        if (missingParticipants.size() == 0 && cost == paid) {
            if (finalizationDate == "-") {
                this.dateFinalized = LocalDate.now().toString();
            }
            this.dateFinalized = LocalDate.parse(finalizationDate).toString();
        }
        return this;
    }

    /**
     *{ @return project finalization status}
     */
    public COMPLETION_STATUS getStatus() {
            if (dateFinalized != null) {
                return COMPLETION_STATUS.FINALIZED;
            }
            else if (dueDate != null && LocalDate.parse(dueDate).isBefore(LocalDate.now())) {
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
        for (String role: requiredParticipants) {
            participant = get(role);
            invoiceBuilder
                .append("---")
                .append(role)
                .append(" Details---\n")
                .append(participant == null ? "N/A" : participant)
                .append('\n');
        }
        invoiceBuilder.append("---Account Details---\n")
            .append(String.format("""
                Total Cost:            R%.2f
                Total Paid:            R%.2f
                Outstanding Balance:   R%.2f\n
                """, cost, paid, cost - paid));
        return invoiceBuilder
                .toString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        String projectDetails = String.format("""
                ----PROJECT DETAILS----
                Project Name:       %s
                Project Address:    %s
                Project Type:       %s
                ERF Number:     %d
                """, getName(), address, getType(), getNumber());
        return new StringBuilder()
                .append(projectDetails)
                .append(getInvoice())
                .toString();
    }
}
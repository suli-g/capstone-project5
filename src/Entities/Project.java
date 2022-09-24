package Entities;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Represents a project
 */
public class Project extends Entity {
    /**
     * The String to use in place of {@code null}.
     */
    public static final String NOT_SET = "N/A";
    /**
     * The total amount owed for this project's finalization.
     */
    private double cost = 0, 
    /**
     * The total amount paid for this project's finalization
     */
    paid = 0;
    private LocalDate dueDate, dateFinalized = null;

    /**
     * @param cost of this Project.
     * @return this Project object.
     */
    public Project setCost(double cost) {
        this.cost = cost;
        return this;
    }

    /**
     * @param cost of this Project as stored in the database.
     * @return this Project object.
     */
    public Project setCost(int cost) {
        this.cost = cost / 100;
        return this;
    }

    /**
     * @return this Project object.
     */
    public Project markFinalized() {
        this.dateFinalized = LocalDate.now();
        return this;
    }

    /**
     * @param date the project was finalized.
     * @return this Project object
     * @throws DateTimeParseException if {@code date} cannot be parsed to a date.
     */
    public Project setDateFinalized(String date) throws DateTimeParseException {
        if (date != null) {
            this.dateFinalized = LocalDate.parse(date);
        }
        return this;
    }

    /**
     * The ERF number of the project's address.
     */
    private int erfNumber;

    /**
     * Project constructor.
     * 
     * @param projectNumber  the phone number of this project's customer.
     * @param projectName    the name of this project.
     * @param projectAddress the address of this project.
     * @param buildingType   the type of building this project represents.
     */
    public Project(int projectNumber, String projectName, String projectAddress, String buildingType) {
        super(projectName, projectAddress, buildingType, projectNumber);
    }

    /**
     * Sets this Project's due date.
     * 
     * @param date the due date as a string.
     * @return this Project object.
     * @throws DateTimeParseException if {@code date} does not represent a valid
     *                                date.
     */
    public Project setDueDate(String date) throws DateTimeParseException {
        // First validate the details of the new date before assigning it.
        if (date != null) {
            dueDate = LocalDate.parse(date);
        }
        return this;
    }

    /**
     * Sets the total amount paid for this project.
     * 
     * @param amount the amount paid in rands.
     * @return this Project object.
     */
    public Project setPaid(double amount) {
        paid = amount;
        return this;
    }

    /**
     * Sets the total amount paid for this project as stored in the database.
     * 
     * @param amount the amount paid.
     * @return this Project object.
     */
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
     * @return the cost of this project.
     */
    public double getCost() {
        return cost;
    }

    /**
     * @return the due date of this project.
     */
    public String getDueDate() {
        return dueDate == null ? NOT_SET : dueDate.toString();
    }

    /**
     * @return the date this project was finalized.
     */
    public String getDateFinalized() {
        return dateFinalized == null ? NOT_SET : dateFinalized.toString();
    }

    /**
     * @return the address ERF number of this project.
     */
    public int getErfNumber() {
        return erfNumber;
    }

    /**
     * @param erfNumber the new ERF number.
     * @return this project object.
     */
    public Project setErfNumber(int erfNumber) {
        this.erfNumber = erfNumber;
        return this;
    }

    /**
     * @return the details of this project .
     */
    @Override
    public String toString() {
        return new StringBuilder(number)
            .append("\n")
            .append(name)
            .append("\n")
            .append(address)
            .append("\n")
            .append(type)
            .append("\n")
            .append(erfNumber)
            .append("\n")
            .append(cost)
            .append("\n")
            .append(paid)
            .append("\n")
            .append(getDueDate())
            .append("\n")
            .append(getDateFinalized())
            .append("\n")
            .toString();
    }
}
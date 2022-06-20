package Entities;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a project
 */
public class Project extends Entity {
    // The format to use for all project-related date strings.
    private static final String DATE_FORMAT = "yyyy/MM/dd";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private Person customer,
            contractor,
            architect;
    private double cost,
            paid = 0;

    private boolean isFinalized = false;
    private String dueDate,
            dateFinalized;

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
    }

    /**
     * Sets the project due date.
     * 
     * @param year       the year at which this project is due.
     * @param month      the month at which this project is due.
     * @param dayOfMonth the day of the month at which this project is due.
     * @return this project.
     */
    public Project setDueDate(int year, int month, int dayOfMonth) {
        // First validate the details of the new date before assigning it.
        LocalDate dateDue = LocalDate.of(year, month, dayOfMonth);
        dueDate = DATE_FORMATTER.format(dateDue);
        return this;
    }

    /**
     * Sets the customer for this project.
     * 
     * @param newCustomer the customer.
     * @return this project.
     */
    public Project setCustomer(Person customer) {
        this.customer = customer;
        return this;
    }

    /**
     * Sets the contractor for this project.
     * 
     * @param contractor the contractor.
     * @return this project.
     */
    public Project setContractor(Person contractor) {
        this.contractor = contractor;
        return this;
    }

    /**
     * Sets the architect for this project.
     * 
     * @param architect the architect.
     * @return this project.
     */
    public Project setArchitect(Person architect) {
        this.architect = architect;
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
     * @return the customer.
     */
    public Person getCustomer() {
        return customer;
    }

    /**
     * @return the contractor.
     */
    public Person getContractor() {
        return contractor;
    }

    /**
     * @return the architect.
     */
    public Person getArchitect() {
        return architect;
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
        return dueDate;
    }

    /**
     * @return the date this project was finalized.
     */
    public String dateFinalized() {
        return dateFinalized;
    }

    /**
     * Marks the project as finalized if all costs have been paid.
     * 
     * @return the finalization status of the project.
     */
    public boolean markFinalized() {
        isFinalized = true;
        dateFinalized = DATE_FORMATTER.format(LocalDate.now());
        return cost != paid;
    }

    /**
     * @return whether this project is finalized or not.
     */
    public boolean getIsFinalized() {
        return isFinalized;
    }

    /**
     * Gets an invoice for this project containing the customer and account details.
     * 
     * @return the project invoice.
     */
    public String getInvoice() {
        String receipt = String.format("""
            Total Cost:            R%.2f
            Total Paid:            R%.2f
            Outstanding Balance:   R%.2f

            """, cost, paid, cost - paid);
        return new StringBuilder()
                .append("---Customer Details---\n")
                .append(customer)
                .append("---Account Details---\n")
                .append(receipt)
                .toString();
    }

    @Override
    public String getName() {
        if (name == "") {
            return type + ' ' + customer.getLastName();
        }
        return name;
    }

    @Override
    public String toString() {
        String projectDetails = String.format("""
                ----PROJECT DETAILS----
                Project Name:       %s
                Project Address:    %s
                Project Type:       %s
                Project Number:     %d
                Project Price:      %.2f

                """, getName(), address, getType(), getNumber(), getCost());
        return new StringBuilder()
                .append(projectDetails)
                .append(getInvoice())

                .append("---Architect---\n")
                .append(architect)

                .append("---Contractor---\n")
                .append(contractor)

                .toString();
    }
}
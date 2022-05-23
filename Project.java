import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a project
 */
public class Project extends Entity{
    // The format to use for all project-related date strings.
    private static final String DATE_FORMAT = "yyyy/MM/dd";
    // A simple cosmetic divider to be used when printing details.

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    Person customer,
        contractor,
        architect;
    private float cost, 
        paid = 0;
    
    boolean isFinalized = false;
    String dueDate, 
        dateFinalized;

    public Project(String projectName, String projectAddress, String projectType, int erfNumber, float totalCost) {
        super(projectName, projectAddress, projectType, erfNumber);
        cost = totalCost;
    }

    public Project setDueDate(int year, int month, int dayOfMonth) {
        // First validate the details of the new date before assigning it.
        LocalDate dateDue = LocalDate.of(year, month, dayOfMonth);
        dueDate = DATE_FORMATTER.format(dateDue);
        return this;
    }

    public Project setCustomer(Person newCustomer) {
        customer = newCustomer;
        return this;
    }

    public Project setContractor(Person newContractor) {
        contractor = newContractor;
        return this;
    }

    public Project setArchitect(Person newArchitect) {
        architect = newArchitect;
        return this;
    }

    public Project setPaid(float amount) {
        paid = amount;
        return this;
    }

    public Person getCustomer() {
        return customer;
    }

    public Person getContractor() {
        return contractor;
    }

    public Person getArchitect() {
        return architect;
    }

    public float getPaid() {
        return paid;
    }

    public float getCost() {
        return cost;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String dateFinalized() {
        return dateFinalized;
    }

    public boolean markFinalized() {
        isFinalized = true;
        dateFinalized = DATE_FORMATTER.format(LocalDate.now());
        return cost != paid;
    }

    
    public String getInvoice() {
        return new StringBuilder()
        .append("---Customer Details---\n")
        .append(customer)
        .append("---Account Details---\n")
        .append(
        String.format("""
        Total Cost:            R%.2f
        Total Paid:            R%.2f
        Outstanding Balance:   R%.2f

        """, cost, paid, cost - paid))
        .toString();
    }

    @Override
    public String getName(){
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
package Components;

import Entities.Person;
import Entities.Project;

/**
 * Represents a ProjectOverview
 */
public class ProjectOverview {
    private static final String OVERVIEW_FORMAT = """
            project number:             %-8d
            project name:               %-8s
            project cost:               R%.2f
            total paid to date:         R%.2f
            customer name:              %-8s
            customer contact number:    %-8s
            contractor name:            %-8s
            contractor contact number:  %-8s
            architect name:             %-8s
            architect contact number:   %-8s
            completion status:          %-8s
            date finalized:             %-8s
            """;
    private int projectNumber;
    private Project project;

    /**
     * Creates a ProjectOverview object.
     * 
     * @param projectNumber the project number to assign to {@link #project}
     * @param project the project to present an overview of.
     */
    public ProjectOverview(int projectNumber, Project project) {
        this.project = project;
        this.projectNumber = projectNumber;
    }

    public String toString() {
        Project.PROJECT_STATUS_ENUM status = project.getStatus();
        String dateFinalized = "n/a";
        if (status == Project.PROJECT_STATUS_ENUM.FINALIZED) {
            dateFinalized = project.getDateFinalized();
        }
        Person customer = project.getPerson("Customer");
        Person contractor = project.getPerson("Contractor");
        Person architect = project.getPerson("Customer");
        return new StringBuilder()
                .append("-")
                .append("\n")
                .append(String.format(OVERVIEW_FORMAT, 
                    projectNumber,
                    project.getName(),
                    project.getCost(),
                    project.getPaid(),
                    customer == null ? "n/a" : customer.getName(),
                    customer == null ? "n/a" : customer.getPhoneNumber(),
                    contractor == null ? "n/a" : contractor.getName(),
                    contractor == null ? "n/a" : contractor.getPhoneNumber(),
                    architect == null ? "n/a" : architect.getName(),
                    architect == null ? "n/a" : architect.getPhoneNumber(),
                    status.label,
                    dateFinalized
                    ))
                .append("-")
                .toString();
    }
}
package Factories;

import java.util.ArrayList;
import Components.ProjectOverview;
import Entities.Project;
import IO.Input;

/**
 * Represents a ProjectFactory
 */
public class ProjectFactory {
    /**
     * Creates a new {@link Project} object using
     * {@link Factories.EntityFactory#addProject(String, int, String, String, double, double, String)}.
     * 
     * @param projectName the name of the project
     * @param projectType the type of the project
     * @return the project with {@code name == projectName}
     */
    public static Project create(String projectName, String projectType) {
        int erfNumber;
        String projectAddress,
                dateFinalized = "-";
        double projectCost, amountPaid;
        while (true) {
            Project thisProject = EntityFactory.getProjectByName(projectName);
            if (thisProject != null) {
                /*
                 * Since project names ought to be unique, if the name matches
                 * then return the project with that name.
                 */
                System.out.println("A project with the name '" + projectName + "' already exists.");
                return thisProject;
            }
            try {
                erfNumber = Input.expect("Project ERF Number").toInteger();
                projectAddress = Input.expect("Project address").toString();
                projectCost = Input.expect("Project Cost").toDouble();
                amountPaid = Input.expect("Amount paid").toDouble();
            } catch (IllegalStateException error) {
                // Just cancel the operation if a required field is skipped.
                System.out.println(error.getLocalizedMessage());
                continue;
            } catch (NumberFormatException error) {
                System.out.println(error.getLocalizedMessage());
                continue;
            }
            break;
        }
        return EntityFactory.addProject(projectName, erfNumber, projectType, projectAddress, projectCost,
                amountPaid, dateFinalized);
    }

    
    /**
     * Lists all projects in with {@code project.getStatus() == completionStatus},
     * or all projects if {@code completionStatus ==null}.
     * 
     * @param completionStatus the completion status to match.
     */
    public static void list(Project.PROJECT_STATUS_ENUM completionStatus) {
        ArrayList<Project> projectList = EntityFactory.getProjects();
        if (projectList.size() > 0) {
            ProjectOverview overview;
            Project currentProject;
            for (int projectNumber = 0; projectNumber < projectList.size(); projectNumber++) {
                currentProject = EntityFactory.getProjectById(projectNumber);
                if (completionStatus == null || completionStatus == currentProject.getStatus()) {
                    overview = new ProjectOverview(projectNumber, currentProject);
                    System.out.println(overview.toString());
                }
            }
        } else {
            System.out.println("No projects have been saved.");
        }
    }

    /**
     * Prints a list of all projects returned from {@link EntityFactory#getProjects()}
     */
    public static void list() {
        list(null);
    }

    /**
     * Prompts the user to create a {@link Entities.Person} for all roles 
     * not currently associated with {@code project}.
     * 
     * @param project the project to check for missing roles.
     */
    public static void fixMissingRoles(Project project) {
        for (String role: project.getMissingRoles()) {
            project.setPerson(role, PersonFactory.create(role));
        }
    }
}

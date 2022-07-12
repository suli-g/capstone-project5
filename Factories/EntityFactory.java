package Factories;

import java.util.ArrayList;
import java.util.HashMap;
import Entities.Person;
import Entities.Project;
import Enums.COMPLETION_STATUS;

/**
 * Defines EntityFactory.
 */
public class EntityFactory {
    private static HashMap<String, Project> projects = new HashMap<>();
    private static ArrayList<String> projectNames = new ArrayList<>();
    private static HashMap<Integer, Person> people = new HashMap<>();

    /**
     * Prompts the user for project details, then creates and stores Project object.
     * 
     * @return the created project
     */
    public static Project addProject(String projectName, int erfNumber, String projectType, String projectAddress,
            double projectCost, double amountPaid, String dateFinalized) {
        Project project = new Project(projectName, projectAddress, projectType, erfNumber, projectCost)
                .setPaid(amountPaid)
                .markFinalized(dateFinalized);

        return addProject(project);
    }

    public static Project addProject(Project project) {
        projectNames.add(project.getName());
        projects.put(project.getName(), project);
        return project;
    }

    /**
     * Prompts the user for a Person's details, then creates and stores
     * 
     * @param position
     * @return the created person
     */
    public static Person addPerson(int phoneNumber, String firstName, String lastName, String physicalAddress,
            String emailAddress) {
        Person person = new Person(firstName, lastName, physicalAddress, emailAddress, phoneNumber);
        return addPerson(person);
    }

    public static Person addPerson(Person person) {
        people.put(person.getNumber(), person);
        return person;
    }

    public static Person getPerson(int phoneNumber) {
        return people.get(phoneNumber);
    }

    public static Project getProjectByName(String projectName) {
        return projects.get(projectName);
    }

    public static Project getProjectById(int projectNumber) {
        if (projectNumber >= projectNames.size()) {
            return null;
        }
        String projectName = projectNames.get(projectNumber);
        return projects.get(projectName);
    }

    public static ArrayList<String> listProjectOverviews() {
        if (projects.size() > 0){
            ArrayList<String> projectOverviews = new ArrayList<>();
            projectOverviews.add(String.format(ProjectOverview.OVERVIEW_FORMAT, "#", "name", "customer name", "customer phone number", "status", "date finalized"));
            ProjectOverview overview;
            for (int projectNumber = 0; projectNumber < projects.size(); projectNumber++) { 
                overview = new ProjectOverview(projectNumber, getProjectById(projectNumber));
                projectOverviews.add(overview.toString());
            }
            return projectOverviews;
        }
        return null;
    }

    public static HashMap<String, Project> getProjects() {
        return projects;
    }

    public static HashMap<Integer, Person> getPeople() {
        return people;
    }
}

class ProjectOverview {
    public static final String OVERVIEW_FORMAT = "| %-3s | %-32s | %-32s | %-21s | %-16s | %-14s |";
    private int projectNumber;
    private Project project;

    public ProjectOverview(int projectNumber, Project project) {
        this.project = project;
        this.projectNumber = projectNumber;
    }

    public String toString() {
        COMPLETION_STATUS status = project.getStatus();
        String dateFinalized = "-";
        if (status == COMPLETION_STATUS.FINALIZED) {
            dateFinalized = project.getDateFinalized();
        }
        Person customer = project.get("Customer");
        return String.format(OVERVIEW_FORMAT, Integer.toString(projectNumber), project.getName(), customer.getName(), customer.getPhoneNumber(), project.getName(), status.label, dateFinalized);
    }
}
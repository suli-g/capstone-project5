package Factories;

import java.util.ArrayList;
import java.util.HashMap;
import Entities.Person;
import Entities.Project;

/**
 * Defines EntityFactory.
 */
public class EntityFactory {
    private static ArrayList<Project> projects = new ArrayList<>();
    private static ArrayList<String> projectNames = new ArrayList<>();
    private static HashMap<Integer, Person> people = new HashMap<>();

    /**
     * Prompts the user for project details, then creates and stores Project object.
     * 
     * @return the created project
     */
    public static Project addProject(String projectName, int erfNumber, String projectType, String projectAddress,
            double projectCost, double amountPaid, String dateFinalized) {
                Project project;
                project = new Project(projectName, projectAddress, projectType, erfNumber, projectCost)
                       .setPaid(amountPaid);
        try {
            project.markFinalized(dateFinalized);
        } catch(IllegalStateException ignore){}

        return addProject(project);
    }

    /**
     * Adds a project to the project list.
     * 
     * @param project the project to add.
     * @return the added project.
     */
    public static Project addProject(Project project) {
        String projectName = project.getName();
        projectNames.add(projectName);
        projects.add(project);
        // Make sure the project was added.
        return projects.get(projectNames.indexOf(projectName));
    }

    /**
     * Adds a new {@link Entities.Person} object to {@link #people} using
     * {@link #assignPerson(Person)}
     * 
     * @param phoneNumber     of the person
     * @param firstName       of the person
     * @param lastName        of the person
     * @param physicalAddress of the person
     * @param emailAddress    of the person
     * @return the person
     */
    public static Person addPerson(int phoneNumber, String firstName, String lastName, String physicalAddress,
            String emailAddress) {
        Person person = new Person(firstName, lastName, physicalAddress, emailAddress, phoneNumber);
        return assignPerson(person);
    }

    /**
     * Assigns a {@link Entities.Person} object to the result of
     * {@link Entities.Person#getNumber()} in {@link #people}
     * on that object.
     * 
     * @param person the assigned person
     * @return
     */
    public static Person assignPerson(Person person) {
        people.put(person.getNumber(), person);
        return person;
    }

    /**
     * Retrieves a {@link Entities.Person} object from {@link #people} using the
     * given {@code phoneNumber}.
     * 
     * @param phoneNumber of the person to retrieve.
     * @return the {@link Entities.Person} linked to {@code phoneNumber}.
     */
    public static Person getPerson(int phoneNumber) {
        return people.get(phoneNumber);
    }

    /**
     * Retrieves the {@link Entities.Project} assigned to {@code projectName} from {@link #projects}.
     * 
     * @param projectName name of the project to retrieve.
     * @return the project object.
     */
    public static Project getProjectByName(String projectName) {
        int projectIndex = projectNames.indexOf(projectName);
        if (projectIndex == -1) {
            return null;
        }
        return projects.get(projectIndex);
    }

    /**
     * Retrieves the {@link Entities.Project} with index {@code projectNumber} from {@link #projects}.
     * 
     * @param projectNumber
     * @return
     */
    public static Project getProjectById(int projectNumber) {
        if (projectNumber >= projects.size()) {
            return null;
        }
        return projects.get(projectNumber);
    }

    /**
     * @return {@link #projects}.
     */
    public static ArrayList<Project> getProjects() {
        return projects;
    }

    /**
     * @return {@link #people}.
     */
    public static HashMap<Integer, Person> getPeople() {
        return people;
    }
}
package Factories;

import java.util.HashMap;
import java.util.NoSuchElementException;

import Entities.Person;
import Entities.Project;
import IO.Input;

/**
 * Defines EntityFactory.
 */
public class EntityFactory {
    private static HashMap<String, Project> projects = new HashMap<>();
    private static HashMap<Integer, Person> people = new HashMap<>();

    /**
     * Prompts the user for project details, then creates and stores Project object.
     * 
     * @return the created project
     */
    public static Project addProject(String projectName, int erfNumber, String projectType, String projectAddress, double projectCost) {
        Project project = new Project(projectName, projectAddress, projectType, erfNumber, projectCost);
        projects.put(projectName, project);
        return project;
    }

    /**
     * Prompts the user for a Person's details, then creates and stores
     * 
     * @param position
     * @return the created person
     */
    public static Person addPerson(int phoneNumber, String firstName, String lastName, String physicalAddress, String emailAddress) {
        Person person = new Person(firstName, lastName, physicalAddress, emailAddress, phoneNumber);
        people.put(phoneNumber, person);
        return person;
    }

    public static Person getPerson(int phoneNumber) {
        return people.get(phoneNumber);
    }

    public static Project getProject(String projectName) {
        return projects.get(projectName);
    }
}

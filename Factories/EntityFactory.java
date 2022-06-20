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
    private static HashMap<Integer, Project> projects = new HashMap<>();
    private static HashMap<Integer, Person> people = new HashMap<>();

    /**
     * Prompts the user for project details, then creates and stores Project object.
     * 
     * @return a Project
     */
    protected static Project newProject() {
        int erfNumber;
        String projectName,
                projectType,
                projectAddress;
        double projectCost;
        try {
            erfNumber = Input.expect("Project ERF Number").toInteger();
            if (projects.containsKey(erfNumber)) {
                /*
                 * Since ERF numbers are unique, if the erf matches
                 * then return the project with that ERF.
                 */
                return projects.get(erfNumber);
            }
            projectName = Input.query("Project Name").toString();
            projectType = Input.expect("Project Type").toString();
            projectAddress = Input.expect("Project address").toString();
            projectCost = Input.expect("Project Cost").toDouble();
        } catch (IllegalStateException error) {
            // Just cancel the operation if a required field is skipped.
            System.out.println(error);
            return null;
        } catch (NoSuchElementException error) {
            // Just cancel the operation if a required field is skipped.
            System.out.println("\nInput cancelled unexpectedly.");
            System.exit(1);
            return null;
        }

        Project project = new Project(projectName, projectAddress, projectType, erfNumber, projectCost);
        projects.put(erfNumber, project);
        return project;
    }

    /**
     * Prompts the user for a Person's details, then creates and stores
     * 
     * @param position
     * @return
     */
    protected static Person newPerson(String position) {
        System.out.println("Setting details for " + position + ": ");
        String firstName,
                lastName,
                physicalAddress,
                emailAddress;
        int phoneNumber;
        try {
            phoneNumber = Input.expect("Phone number").toInteger();
            /*
             * Since phone numbers are unique, return a person if there is a
             * phone number that matches.
             */
            if (people.containsKey(phoneNumber)) {
                return people.get(phoneNumber);
            }

            firstName = Input.expect("First name").toString();
            lastName = Input.expect("Last name").toString();
            physicalAddress = Input.expect("Physical address").toString();
            emailAddress = Input.expect("Email address").toString();
        } catch (IllegalStateException error) {
            // Just cancel the operation if a required field is skipped.
            System.out.println(error);
            return null;
        } catch (NoSuchElementException error) {
            // Just cancel the operation if a required field is skipped.
            System.out.println("\nInput cancelled unexpectedly.");
            System.exit(1);
            return null;
        }
        Person person = new Person(firstName, lastName, physicalAddress, emailAddress, phoneNumber);
        people.put(phoneNumber, person);
        return person;
    }

    protected Person getPerson(int phoneNumber) {
        return people.get(phoneNumber);
    }

    protected Project getProject(int erfNumber) {
        return projects.get(erfNumber);
    }
}

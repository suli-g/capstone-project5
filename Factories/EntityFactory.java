package Factories;

import java.util.HashMap;

import Controller.Interactions.Input;
import Entities.Person;
import Entities.Project;

public class EntityFactory {
    private static HashMap<Integer, Project> projects = new HashMap<>();
    private static HashMap<Integer, Person> people = new HashMap<>();
    protected static Project newProject() {
        int erfNumber = Input.expect("Project ERF Number").toInteger();
        if (projects.containsKey(erfNumber)) {
            return projects.get(erfNumber);
        }
        String projectName = Input.query("Project Name").toString();
        String projectType = Input.expect("Project Type").toString();
        String projectAddress = Input.expect("Project address").toString();
        double projectCost = Input.expect("Project Cost").toDouble();
        Project project = new Project(projectName, projectAddress, projectType, erfNumber, projectCost);
        projects.put(erfNumber, project);
        return project;
    }

    protected static Person newPerson(String position) {
        System.out.println("Setting details for " + position + ": ");
        int phoneNumber = Input.expect("Phone number").toInteger();
        if (people.containsKey(phoneNumber)) {
            return people.get(phoneNumber);
        }
        String firstName = Input.expect("First name").toString(), 
            lastName = Input.expect("Last name").toString(),
            physicalAddress = Input.expect("Physical address").toString(),
            emailAddress = Input.expect("Email address").toString();
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

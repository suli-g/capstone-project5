package Factories;

import java.util.HashMap;

import Components.Prompter;
import Entities.Person;
import Entities.Project;

public class EntityFactory {
    private static HashMap<Integer, Project> projects = new HashMap<>();
    private static HashMap<Integer, Person> people = new HashMap<>();
    protected static Project newProject() {
        int erfNumber = Prompter.expect("Project ERF Number").toInteger();
        if (projects.containsKey(erfNumber)) {
            return projects.get(erfNumber);
        }
        String projectName = Prompter.query("Project Name").toString();
        String projectType = Prompter.expect("Project Type").toString();
        String projectAddress = Prompter.expect("Project address").toString();
        double projectCost = Prompter.expect("Project Cost").toDouble();
        Project project = new Project(projectName, projectAddress, projectType, erfNumber, projectCost);
        projects.put(erfNumber, project);
        return project;
    }

    protected static Person newPerson(String position) {
        System.out.println("Setting details for " + position + ": ");
        int phoneNumber = Prompter.expect("Phone number").toInteger();
        if (people.containsKey(phoneNumber)) {
            return people.get(phoneNumber);
        }
        String firstName = Prompter.expect("First name").toString(), 
            lastName = Prompter.expect("Last name").toString(),
            physicalAddress = Prompter.expect("Physical address").toString(),
            emailAddress = Prompter.expect("Email address").toString();
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

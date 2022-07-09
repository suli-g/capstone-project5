package Factories;

import java.io.IOException;
import Entities.Person;
import Entities.Project;
import IO.DataSource;

public class EntityModel {
    private static EntityModel encoderInstance;
    protected static DataSource people, projects;
    private static String separator;

    private EntityModel(DataSource projectSource, DataSource peopleSource, String delimiter) {
        separator = delimiter;
        projects = projectSource;
        people = peopleSource;
    }

    public static DataSource getPeopleSource() {
        return people;
    }

    public static DataSource getProjectSource() {
        return projects;
    }

    public static EntityModel getInstance(DataSource projectSource, DataSource peopleSource, String delimiter) {
        if (encoderInstance == null) {
            encoderInstance = new EntityModel(projectSource, peopleSource, delimiter);
        }
        return encoderInstance;
    }

    private static String[] decode(String data) {
        return data.split(separator);
    }

    public static Project getProject(String data) throws NumberFormatException {
        String[] segments = decode(data);
        String projectName = segments[0];
        int erfNumber = Integer.parseInt(segments[1]);
        String projectType = segments[2];
        String projectAddress = segments[3];
        double projectCost = Double.parseDouble(segments[4]);
        return EntityFactory.addProject(projectName, erfNumber, projectType, projectAddress, projectCost);
    }

    public static Project addProject(String projectName, int erfNumber, String projectType, String projectAddress,
            double projectCost) throws IOException {
                Project project = EntityFactory.addProject(projectName, erfNumber, projectType, projectAddress, projectCost);
        String projectData = new StringBuilder()
                .append(projectName)
                .append(separator)
                .append(erfNumber)
                .append(separator)
                .append(projectType)
                .append(separator)
                .append(projectAddress)
                .append(separator)
                .append(projectCost)
                .append("\n")
                .toString();
        projects.addLine(projectData);
        System.out.println(projectData);
        return project;
    }

    public static Person getPerson(String data) {
        String[] segments = decode(data);
        int phoneNumber = Integer.parseInt(segments[0]);
        String firstName = segments[1];
        String lastName = segments[2];
        String physicalAddress = segments[3];
        String emailAddress = segments[4];
        return EntityFactory.addPerson(phoneNumber, firstName, lastName, physicalAddress, emailAddress);
    }

    public static Person addPerson(int phoneNumber, String firstName, String lastName, String physicalAddress,
            String emailAddress) throws IOException {
        Person person = EntityFactory.addPerson(phoneNumber, firstName, lastName, physicalAddress, emailAddress);
        String personData = new StringBuilder()
                .append(person.getPhoneNumber())
                .append(separator)
                .append(person.getName())
                .append(separator)
                .append(person.getLastName())
                .append(separator)
                .append(person.getAddress())
                .append(separator)
                .append(person.getEmailAddress())
                .append("\n")
                .toString();
                System.out.println(personData);
        people.addLine(personData);
        return person;
    }

}

package Factories;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;

import Entities.Person;
import Entities.Project;
import IO.DataSource;

interface DataSourceEntry {
    String VALUE_DELIMITER = ";";
}

public class EntityModel implements DataSourceEntry {
    private static EntityModel encoderInstance;
    protected static DataSource people, projects;

    private EntityModel(DataSource projectSource, DataSource peopleSource) {
        projects = projectSource;
        people = peopleSource;
    }

    public static DataSource getPeopleSource() {
        return people;
    }

    public static DataSource getProjectSource() {
        return projects;
    }

    public static EntityModel getInstance(DataSource projectSource, DataSource peopleSource) {
        if (encoderInstance == null) {
            encoderInstance = new EntityModel(projectSource, peopleSource);
        }
        return encoderInstance;
    }

    public static Project loadProject(String data) throws NumberFormatException, IndexOutOfBoundsException {
        try {
            return ProjectEntry.asProject(data.split(VALUE_DELIMITER));
        } catch (Exception error) {
            System.out.println(error);
            return null;
        }
    }

    public static void saveProjects(HashMap<String, Project> projectMap) throws IOException {
        int entryAmount = projectMap.size();
        if (entryAmount > 0) {
            StringBuilder data = new StringBuilder();
            int projectNumber = 0;
            for (Project project : projectMap.values()) {
                data.append(ProjectEntry.asString(projectNumber++, project))
                        .append(System.lineSeparator());
            }
            projects.saveData(data.toString());
        }
    }

    public static Person loadPerson(String data) throws NumberFormatException, IndexOutOfBoundsException {
        if (data == null) {
            return null;
        }
        return PersonEntry.asPerson(data.split(VALUE_DELIMITER));
    }

    public static void savePeople(HashMap<Integer, Person> peopleMap) throws IOException {
        if (peopleMap.size() > 0) {
            StringBuilder data = new StringBuilder();
            for (Person person : peopleMap.values()) {
                data.append(PersonEntry.asString(person))
                        .append(System.lineSeparator());
            }
            data.deleteCharAt(data.length() - 1);
            people.saveData(data.toString());
        }
    }
}

class DataEntry implements DataSourceEntry {
    StringBuilder value;
    private String[] headers;

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public DataEntry() {
        value = new StringBuilder();
    }

    public DataEntry append(Object data) {
        value.append(data).append(VALUE_DELIMITER);
        return this;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

class PersonEntry {
    private static String[] headers = new String[] {
            "first name", "last name", "physical address", "email address", "phone number"
    };

    public static String[] getHeaders() {
        return headers;
    }

    public static String asString(Person person) {
        return new DataEntry()
                .append(person.getFirstName())
                .append(person.getLastName())
                .append(person.getAddress())
                .append(person.getEmailAddress())
                .append(person.getNumber())
                .toString();
    }

    public static Person asPerson(String[] entry) throws NumberFormatException, IndexOutOfBoundsException {
        return new Person(entry[0], entry[1], entry[2], entry[3], Integer.parseInt(entry[4]));
    }
}

class ProjectEntry implements DataSourceEntry {
    private static String[] headers = new String[] {
            "number", "name", "physical address", "building type", "erf number", "cost", "paid", "finalization date",
            "Customer", "Contractor", "Architect"
    };

    public static String[] getHeaders() {
        return headers;
    }

    public static String asString(int projectNumber, Project project) {
        String finalizationDate = project.getDateFinalized();
        return new DataEntry()
                .append(projectNumber)
                .append(project.getName())
                .append(project.getAddress())
                .append(project.getType())
                .append(project.getNumber())
                .append(project.getCost())
                .append(project.getPaid())
                .append(finalizationDate == null ? "-" : finalizationDate)
                .append(project.get("Customer").getNumber())
                .append(project.get("Contractor").getNumber())
                .append(project.get("Architect").getNumber())
                .toString();
    }

    public static Project asProject(String[] entry) throws NumberFormatException, IndexOutOfBoundsException {
        Project resultProject = new Project(entry[1], entry[2], entry[3], Integer.parseInt(entry[4]),
                Double.parseDouble(entry[5]));
        resultProject.setPaid(Double.parseDouble(entry[6]));
        try {
            resultProject.markFinalized(entry[7]);
        } catch (Exception ignore) {
            // No finalization date available.
        }
        resultProject.set("Customer", EntityFactory.getPerson(Integer.parseInt(entry[8])));
        resultProject.set("Architect", EntityFactory.getPerson(Integer.parseInt(entry[9])));
        resultProject.set("Contractor", EntityFactory.getPerson(Integer.parseInt(entry[10])));
        return resultProject;
    }
}
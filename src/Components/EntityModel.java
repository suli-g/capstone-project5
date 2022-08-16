package Components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Components.DataEntry.DelimitedValueString;
import Components.DataEntry.PersonEntry;
import Components.DataEntry.ProjectEntry;
import Entities.Person;
import Entities.Project;
import IO.DataSource;

/**
 * Represents an EntityModel
 */
public class EntityModel implements DelimitedValueString {
    private static EntityModel modelInstance;
    protected static DataSource peopleDataSource, projectDataSource;

    /**
     * Instantiates an EntityModel as a singleton with 2 {@link DataSource} objects.
     * 
     * @param projectSource the {@link DataSource} for {@link Project} entities.
     * @param peopleSource the {@link DataSource} for {@link Person} entities.
     */
    private EntityModel(DataSource projectSource, DataSource peopleSource) {
        projectDataSource = projectSource;
        peopleDataSource = peopleSource;
    }

    /**
     * Returns the EntityModel instance associated with this Java application, or creates a new EntityModel
     * and associates a {@link DataSource} to each {@link Person} and {@link Project} with the instantiated
     * EntityModel if no EntityModel has been instantiated .
     * 
     * @param projectSource the {@link DataSource} to store {@link Project} information.
     * @param peopleSource the {@link DataSource} to store {@link Person} information.
     * @return the EntityModel associated with this Java application.
     */
    public static EntityModel getInstance(DataSource projectSource, DataSource peopleSource) {
        if (modelInstance == null) {
            modelInstance = new EntityModel(projectSource, peopleSource);
        }
        return modelInstance;
    }

    /**
     * @return the {@link DataSource} that stores all {@link Person} information.
     */
    public static DataSource getPeopleSource() {
        return peopleDataSource;
    }

    /**
     * @return the {@link DataSource} that stores {@link Project} information.
     */
    public static DataSource getProjectSource() {
        return projectDataSource;
    }

    /**
     * Parses a {@code String} to a {@link Project} object using infromation in {@link #projectDataSource}.
     * 
     * @param data the string to parse.
     * @return a {@link Project} object.
     * @throws NumberFormatException if a number in {@code data} is incorrectly formatted.
     * @throws IndexOutOfBoundsException if the amount of delimited values in {@code data} is less than required.
     */
    public static Project parseProject(String data) throws NumberFormatException, IndexOutOfBoundsException {
        return ProjectEntry.parse(data);
    }

    /**
     * Converts {@link Project} objects stored in {@code projectList} into {@code String} values and writes
     * them to {@link #projectDataSource}.
     * 
     * @param projectList an ArrayList of {@link Project} objects.
     * @throws IOException if {@link #projectDataSource} fails to write the data.
     */
    public static void unparseProjects(ArrayList<Project> projectList) throws IOException {
        int entryAmount = projectList.size();
        System.out.println(projectList);
        if (entryAmount > 0) {
            StringBuilder data = new StringBuilder();
            int projectNumber = 0;
            for (Project project : projectList) {
                data.append(ProjectEntry.unparse(projectNumber, project))
                        .append(System.lineSeparator());
                projectNumber++;
            }
            projectDataSource.write(data.toString());
        }
    }

    /**
     * Parses a {@code String} to a {@link Person} object using infromation in {@link #peopleDataSource}.
     * 
     * @param data the {@code String} to be parsed.
     * @return a new {@link Person} object.
     * @throws NumberFormatException if a number {@code data} cannot be parsed.
     * @throws IndexOutOfBoundsException if the amount of delimited values in {@code data} is less than required.
     */
    public static Person parsePerson(String data) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (data == null) {
            return null;
        }
        return PersonEntry.parse(data);
    }

    /**
     * Converts {@link Person} objects stored in {@code personMap} into {@code String} values and writes
     * them to {@link #peopleDataSource}.
     * 
     * @param peopleMap A mapping of phone numbers to {@link Person} objects.
     * @throws IOException if {@link #peopleDataSource} fails to write the data.
     */
    public static void unparsePeople(HashMap<Integer, Person> peopleMap) throws IOException {
        if (peopleMap.size() > 0) {
            StringBuilder data = new StringBuilder();
            for (Person person : peopleMap.values()) {
                data.append(PersonEntry.unparse(person))
                        .append(System.lineSeparator());
            }
            peopleDataSource.write(data.toString());
        }
    }
}

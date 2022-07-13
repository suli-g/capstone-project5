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
     * Instantiates an EntityModel as a singleton with 2 {@link IO.DataSource} objects.
     * 
     * @param projectSource the {@link IO.DataSource} for {@link Entities.Project} entities.
     * @param peopleSource the {@link IO.DataSource} for {@link Entities.Person} entities.
     */
    private EntityModel(DataSource projectSource, DataSource peopleSource) {
        projectDataSource = projectSource;
        peopleDataSource = peopleSource;
    }

    /**
     * @return the {@link IO.DataSource} that stores {@link Entities.People} information.
     */
    public static DataSource getPeopleSource() {
        return peopleDataSource;
    }

    /**
     * @return the {@link IO.DataSource} that stores {@link Entities.Project} information.
     */
    public static DataSource getProjectSource() {
        return projectDataSource;
    }

    /**
     * 
     * @param projectSource the {@link IO.DataSource} to store {@link Entities.Project} information.
     * @param peopleSource the {@link IO.DataSource} to store {@link Entities.Person} information.
     * @return
     */
    public static EntityModel getInstance(DataSource projectSource, DataSource peopleSource) {
        if (modelInstance == null) {
            modelInstance = new EntityModel(projectSource, peopleSource);
        }
        return modelInstance;
    }

    /**
     * Parses a {@code String} to a {@link Entities.Project} object using infromation in {@link #projectDataSource}.
     * 
     * @param data
     * @return a {@link Entities.Project} object
     * @throws IllegalArgumentException if the {@code data} is malformed.
     * @throws IndexOutOfBoundsException if the amount of delimited values in {@value data} is less than required.
     */
    public static Project parseProject(String data) throws IllegalArgumentException, IndexOutOfBoundsException {
        try {
            return ProjectEntry.parse(data);
        } catch(NumberFormatException error) {
            throw new IllegalArgumentException("The data is malformed.");
        }
        catch (Exception error) {
            System.out.println(error);
            return null;
        } 
    }

    /**
     * Converts {@link Entities.Project} objects stored in {@code projectList} into {@code String} values and writes
     * them to {@link #projectDataSource}.
     * 
     * @param projectList an ArrayList of {@link Entities.Project} objects.
     * @throws IOException if {@link #projectDataSource} fails to write the data.
     */
    public static void unparseProjects(ArrayList<Project> projectList) throws IOException {
        int entryAmount = projectList.size();
        if (entryAmount > 0) {
            StringBuilder data = new StringBuilder();
            int projectNumber = 0;
            for (Project project : projectList) {
                data.append(ProjectEntry.unparse(projectNumber++, project))
                        .append(System.lineSeparator());
            }
            projectDataSource.write(data.toString());
        }
    }

    /**
     * Parses a {@code String} to a {@link Entities.Person} object using infromation in {@link #peopleDataSource}.
     * 
     * @param data the {@code String} to be parsed.
     * @return a new {@link Entities.Person} object
     * @throws IllegalArgumentException if {@code data} is malformed.
     * @throws IndexOutOfBoundsException if the amount of delimited values in {@value data} is less than required.
     */
    public static Person parsePerson(String data) throws IllegalArgumentException, IndexOutOfBoundsException {
        try {
            if (data == null) {
                return null;
            }
            return PersonEntry.parse(data);
        } catch(NumberFormatException error) {
            throw new IllegalArgumentException("The data is malformed.");
        }
    }

    /**
     * Converts {@link Entities.Person} objects stored in {@code personMap} into {@code String} values and writes
     * them to {@link #peopleDataSource}.
     * 
     * @param peopleMap A mapping of phone numbers to {@link Entities.Person} objects.
     * @throws IOException if {@link #peopleDataSource} fails to write the data.
     */
    public static void unparsePeople(HashMap<Integer, Person> peopleMap) throws IOException {
        if (peopleMap.size() > 0) {
            StringBuilder data = new StringBuilder();
            for (Person person : peopleMap.values()) {
                data.append(PersonEntry.unparse(person))
                        .append(System.lineSeparator());
            }
            data.deleteCharAt(data.length() - 1);
            peopleDataSource.write(data.toString());
        }
    }
}

package Components.DataEntry;

import Entities.Project;
import Factories.EntityFactory;

/**
 * Represents a ProjectEntry
 */
public class ProjectEntry extends EntityEntryBuilder {
    /**
     * Parses a {@link Entities.Person} object into a {@link String}.
     * 
     * @param person the person object to parse.
     * @return a {@link String} representing {@value person}
     */
    public static String unparse(int projectNumber, Project project) {
        String finalizationDate = project.getDateFinalized();
        return entryString
                .append(projectNumber)
                .append(project.getName())
                .append(project.getAddress())
                .append(project.getType())
                .append(project.getNumber())
                .append(project.getCost())
                .append(project.getPaid())
                .append(finalizationDate == null ? "-" : finalizationDate)
                .append(project.getPerson("Customer").getNumber())
                .append(project.getPerson("Contractor").getNumber())
                .append(project.getPerson("Architect").getNumber())
                .toString();
    }

    /**
     * Parses a {@link String} into a {@link Entities.Project} object.
     * 
     * @param data the array to parse.
     * @return a {@link Entities.Project} object
     * @throws NumberFormatException if the values meant to be numbers cannot be parsed as such.
     * @throws IndexOutOfBoundsException If {@code entry} does not have exactly 11 values.
     */
    public static Project parse(String data) throws NumberFormatException, IndexOutOfBoundsException {
        String[] entry = data.split(VALUE_DELIMITER);
        if (entry.length < 11) {
            throw new IndexOutOfBoundsException("The data should have exactly 11 values.");
        }
        Project resultProject = new Project(entry[1], entry[2], entry[3], Integer.parseInt(entry[4]),
                Double.parseDouble(entry[5]));
        resultProject.setPaid(Double.parseDouble(entry[6]));
        try {
            resultProject.markFinalized(entry[7]);
        } catch (Exception ignore) {
            // Project wasn't finalized.
        }
        resultProject.set("Customer", EntityFactory.getPerson(Integer.parseInt(entry[8])));
        resultProject.set("Architect", EntityFactory.getPerson(Integer.parseInt(entry[9])));
        resultProject.set("Contractor", EntityFactory.getPerson(Integer.parseInt(entry[10])));
        return resultProject;
    }
}

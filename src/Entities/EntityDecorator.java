package Entities;

import Interfaces.EntityFormatter;

/**
 * Displays {@link Entity} objects in readable formats as defined in
 * {@link EntityFormatter}.
 */
public class EntityDecorator implements EntityFormatter {
    /**
     * Prints the details of an entity to the terminal in a format
     * 
     * @param entity an object that implements {@link Entity}.
     * @return a formatted string representation of {@code entity}.
     */
    public static String decorate(Entity entity) {
        if (entity instanceof Person) {
            Person person = (Person) entity;
            return String.format(PERSON_OVERVIEW_FORMAT, person.getFirstName(), person.getLastName(),
                    person.getEmailAddress(), person.getPhoneNumber(), person.getAddress());
        } else if (entity instanceof Project) {
            Project project = (Project) entity;

            return new StringBuilder(String.format(PROJECT_OVERVIEW_FORMAT, project.getNumber(), project.getName(),
                    project.getAddress(),
                    project.getType(), project.getErfNumber()))
                    .append(
                            String.format(ACCOUNT_OVERVIEW_FORMAT, project.getCost(), project.getPaid()))
                    .append(
                            String.format(PROGRESS_OVERVIEW_FORMAT, project.getDueDate(), project.getDateFinalized()))
                    .toString();
        }
        return null;
    }
}

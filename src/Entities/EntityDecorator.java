package Entities;

import Interfaces.EntityFormatter;

/**
 * Displays {@link Entity} objects in readable formats as defined in {@link EntityFormatter}.
 */
public class EntityDecorator implements EntityFormatter {
    /**
     * Prints the details of an entity to the terminal in a format 
     * 
     * @param entity an object that implements {@link Entity}.
     */
    public static void printEntity(Entity entity) {
        if (entity instanceof Person) {
            Person person = (Person) entity;
            System.out.printf(PERSON_OVERVIEW_FORMAT, person.getFirstName(), person.getLastName(),
                person.getEmailAddress(), person.getPhoneNumber(), person.getAddress());
        }
        else if (entity instanceof Project) {
            Project project = (Project) entity;
            System.out.printf(PROJECT_OVERVIEW_FORMAT, project.getNumber(), project.getName(), project.getAddress(),
                project.getType(), project.getErfNumber());
        System.out.printf(ACCOUNT_OVERVIEW_FORMAT, project.getCost(), project.getPaid());
        System.out.printf(PROGRESS_OVERVIEW_FORMAT, project.getDueDate(), project.getDateFinalized());
        }
    }
}

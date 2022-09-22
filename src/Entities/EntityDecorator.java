package Entities;

import Interfaces.Overviews;

public class EntityDecorator implements Overviews{
    public static String listProject(Project project) {
        return String.format(PROJECT_OVERVIEW_FORMAT, project.getNumber(), project.getName(), project.getAddress(), project.getType(), project.getErfNumber());
    }

    public static String showProject(Project project) {
        return new StringBuilder(String.format(PROJECT_OVERVIEW_FORMAT, project.getNumber(), project.getName(), project.getAddress(), project.getType(), project.getErfNumber()))
        .append(String.format(ACCOUNT_OVERVIEW_FORMAT, project.getCost(), project.getPaid()))
        .append(String.format(PROGRESS_OVERVIEW_FORMAT, project.getDueDate(), project.getDateFinalized()))
        .toString();
    }

    public static String showPerson(Person person) {
        return String.format(PERSON_OVERVIEW_FORMAT, person.getFirstName(), person.getLastName(), person.getEmailAddress(), person.getPhoneNumber(), person.getAddress());
    }
}

package Builders;

public class EntityBuilder {
    protected static ProjectBuilder addProject(String projectName) {
        return new ProjectBuilder(projectName);
    }

    protected static PersonBuilder addPerson(String firstName, String lastName) {
        return new PersonBuilder(firstName, lastName);
    }
}

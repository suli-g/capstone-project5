package Builders;

import Entities.Person;

public class PersonBuilder {
    private String firstName;
    private String lastName;
    private String physicalAddress;
    private String emailAddress;
    private int phoneNumber;
    public PersonBuilder(String firstName, String lastName) {
        this.withNames(firstName, lastName);
    }

    public PersonBuilder() {
    }

    public PersonBuilder withNames(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        return this;
    }

    public PersonBuilder withPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public PersonBuilder withEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public PersonBuilder withPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
        return this;
    }

    public Person create() {
        return new Person(firstName, lastName, physicalAddress, emailAddress, phoneNumber);
    }

}

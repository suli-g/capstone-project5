package Entities;
/**
 * Represents a person.
 */
public class Person extends Entity {
    private String lastName, firstName, phoneNumber;
    /**
     * Person constructor.
     * 
     * @param firstName       the first name of this person.
     * @param lastName        the last name of this person.
     * @param physicalAddress the physical address of this person.
     * @param emailAddress    the email address of this person.
     * @param phoneNumber     the phone number of this person (leading zeros
     *                        truncated).
     */
    public Person(int personId, String firstName, String lastName, String physicalAddress, String emailAddress) {
        super(firstName, physicalAddress, emailAddress, personId); // store the full name of this                                                       // person as the entity name.
        this.lastName = lastName;
        this.firstName = firstName;
    }

    /**
     * @param emailAddress the new email address of this person.
     * @return this person.
     */
    public Person setEmailAddress(String emailAddress) {
        this.type = emailAddress;
        return this;
    }

    /**
     * @param phoneNumber the new phone number of this person.
     * @return this person.
     */
    public Person setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    /**
     * @return this person's address.
     */
    public String getAddress() {
        return String.format(ADDRESS_OVERVIEW_FORMAT,
        (Object[]) this.address.split(", "));
    }

    /**
     * @return this person's emailAddress.
     */
    public String getEmailAddress() {
        // Return type since is the only field in the parent class which was not used.
        return this.type;
    }

    /**
     * @return this person's number with a leading 0.
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * @return this person's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return this person's lastName.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return this person's full name.
     */
    public String getName() {
        return firstName + ' ' + lastName;
    }

    
    /** 
     * @return the person ID of this person.
     */
    public int getNumber() {
        return number;
    }

    
    /** 
     * @param number new personId for this person
     * @return this Person object.
     */
    public Person setNumber(int number) {
        this.number = number;
        return this;
    }

    /**
     * Stores this Person's details in a logical format.
     */
    @Override
    public String toString() {
        return String.format(
            PERSON_OVERVIEW_FORMAT, firstName, lastName, type, phoneNumber, getAddress());
    }
}
/**
 * Represents a person.
 */
public class Person extends Entity {
    private String lastName;
    public Person(String firstName, String lastName, String physicalAddress, String emailAddress, int phoneNumber) {
        super(firstName + ' ' + lastName, physicalAddress, emailAddress, phoneNumber);
        this.lastName = lastName;
    }

    public Person setEmailAddress(String emailAddress) {
        this.type = emailAddress;
        return this;
    }

    public Person setPhoneNumber(int phoneNumber) {
        number = phoneNumber;
        return this;
    }

    public String getAddress() {
        return this.address;
    }
    public String getEmailAddress() {
        // Return type since is the only field in the parent class which was not useful here.
        return this.type;
    }

    public String getPhoneNumber() {
        /*  Add the missing first digit since all starting zeroes would 
            have been removed in the setter. 
        */
        return "0" + this.number;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("""
                Name:               %s
                Phone Number:       %s
                Address:            %s
                Email Address:      %s
                \n""", getName(), getPhoneNumber(), getAddress(), getEmailAddress());
    }
}
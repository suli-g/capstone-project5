package Factories;


import Entities.Person;
import IO.Input;

/**
 * Represents a PersonFactory
 */
public class PersonFactory {
    private static final String REUSE_PERSON_MESSAGE = "The information given matches some in the database. Use details for %s? (Y/n)";

    /**
     * Creates a new {@link Person} object using
     * {@link Factories.EntityFactory#addPerson(int, String, String, String, String)}.
     * 
     * @param role the role of the person in the project.
     * @return the person created.
     */
    public static Person create(String role) {
        System.out.println("Setting details for " + role + ": ");
        String firstName = null,
                lastName = null,
                physicalAddress = null,
                emailAddress = null;
        Integer phoneNumber = null;
        Person thisPerson;
        while (true) {
            try {
                if (phoneNumber == null)
                    phoneNumber = Input.expect("Phone number").toInteger(9);
                thisPerson = EntityFactory.getPerson(phoneNumber);
                /*
                 * Since phone numbers are unique, return a person if there is a
                 * phone number that matches.
                 */
                if (thisPerson != null) {
                    Input.query(String.format(REUSE_PERSON_MESSAGE, thisPerson.getName()));
                    if (Input.getInstance().toBoolean()) {
                        return thisPerson;
                    } else {
                        phoneNumber = null;
                    }
                    continue;
                }
                if (firstName == null || firstName == "")
                    firstName = Input.expect("First name").toString();
                if (lastName == null || lastName == "")
                    lastName = Input.expect("Last name").toString();
                if (physicalAddress == null || physicalAddress == "")
                    physicalAddress = Input.expect("Physical address").toString();
                if (emailAddress == null || emailAddress == "")
                    emailAddress = Input.expect("Email address").toString();
                return EntityFactory.addPerson(phoneNumber, firstName, lastName, physicalAddress, emailAddress);
            } catch (NumberFormatException error) {
                System.out.println("A number is expected here.");
            } catch (IllegalArgumentException error) {
                System.out.print(error.getLocalizedMessage());
                System.out.println("(with optional leading zero)");
            } catch (IllegalStateException error) {
                System.out.println(error.getLocalizedMessage());
                continue;
            }
        }
    }
}

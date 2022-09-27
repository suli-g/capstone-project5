package Factories;

import java.sql.ResultSet;
import java.sql.SQLException;

import Entities.Person;

/**
 * Represents the structure of a database row containing data about a person.
 */
interface PersonRow {
    /**
     * The column name linked to the id of a person in the database
     */
    String PERSON_ID = "person_id";
    /**
     * The column name linked to the first name of a person in the database
     */
    String FIRST_NAME = "first_name";
    /**
     * The column name linked to the last name of a person in the database
     */
    String LAST_NAME = "last_name";
    /**
     * The column name linked to the physical address of a person in the database
     */
    String PHYSICAL_ADDRESS = "physical_address";
    /**
     * The column name linked to the email address of a person in the database
     */
    String EMAIL_ADDRESS = "email_address";
    /**
     * The column name linked to the phone number of a person in the database
     */
    String PHONE_NUMBER = "phone_number";
    /**
     * The column name linked to the erf number of an address in the database
     */
    String ERF_NUMBER = "erf_number";
}

/**
 * Contains methods that allow easy creation of {@link Person} objects.
 */
public class PersonFactory implements PersonRow {
    /**
     * Creates a new {@link Person} object with the given parameters.
     * 
     * @param results the resultset containing the necessary columns.
     * @return a {@link Person} object with the given parameters.
     * @throws SQLException if {@code results} does not contain the required columns
     *                      database error occurs.
     */
    public static Person fromResultSet(ResultSet results) throws SQLException {
        return new Person(
                results.getInt(PERSON_ID),
                results.getString(FIRST_NAME),
                results.getString(LAST_NAME),
                results.getString(PHYSICAL_ADDRESS),
                results.getString(EMAIL_ADDRESS))
                .setPhoneNumber(results.getString(PHONE_NUMBER))
                .setErfNumber(results.getInt(ERF_NUMBER));
    }

    /**
     * Creates a new {@link Person} object with the given parameters.
     * 
     * @param personId        of the Person
     * @param firstName       of the Person
     * @param lastName        of the Person
     * @param emailAddress    of the Person
     * @param phoneNumber     of the Person
     * @param physicalAddress of the Person
     * @param erfNumber       of {@code physicalAddress}
     * @return a {@link Person} object with the given parameters.
     */
    public static Person fromDetails(int personId, String firstName, String lastName, String emailAddress,
            String phoneNumber, String physicalAddress, int erfNumber) {
        return new Person(personId, firstName, lastName, physicalAddress, emailAddress).setPhoneNumber(phoneNumber).setErfNumber(erfNumber);
    }
}

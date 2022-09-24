package Utilities;

import java.io.IOException;
import java.rmi.AccessException;
import java.sql.SQLDataException;
import java.sql.SQLException;

import Components.Input;
import Controller.EntityController;
import Controller.ParticipantController;
import Entities.Person;

/**
 * Contains helper methods to be used when working with {@link Entity} information. 
 */
public class EntityUtils {
    /**
     * @param entityController the EntityController instance of this application.
     * @param erfNumber the ERF number of the address.
     * @return true if the address exists; false if the address does not exit.
     * @throws SQLException if a database access error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public static boolean addressExists(EntityController entityController, int erfNumber) throws SQLException, IOException {
        String address = entityController.findAddress(erfNumber);
        if (address != null) {
            return true;
        }
        System.out.println("Address not found in database. Address Details: ");
        boolean registration = entityController.registerAddress(erfNumber,
                Input.expect("Street Address").toString(),
                Input.expect("Suburb").toString(),
                Input.expect("City").toString(),
                Input.expect("Province").toString(),
                Input.expect("Post code").toInteger());
        if (!registration) {
            throw new AccessException("A problem occured while registering the address.");
        }
        return registration;
    }

    /**
     * Verifies that all details of a {@link Person} is already stored in the database and, if not, stores the person's details in the database.
     * 
     * @param entityController the EntityController instance of this application.
     * @param role the role {@code person} should be assigned to if not yet assigned.
     * @param person the person whos details need to be verified.
     * @return the {@code person} supplied to this function.
     * @throws SQLException if a database access error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public static Person checkPersonDetails(EntityController entityController, String role, Person person) throws IOException, SQLException {
        ParticipantController participantController = entityController.getParticipantController();
        OutputUtils.printCentered(role.toUpperCase());
        int erfNumber;
        if (person != null) {
            participantController.assignPerson(role, person);
            return person;
        }
        erfNumber = Input.expect("Address ERF Number").toInteger();

        if (!addressExists(entityController, erfNumber)) {
            throw new SQLDataException("No address has been linked to the given ERF number.");
        }
        return person;
    }
}

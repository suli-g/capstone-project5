package Utilities;

import java.io.IOException;
import java.rmi.AccessException;
import java.sql.SQLDataException;
import java.sql.SQLException;

import Components.Input;
import Controller.EntityManager;
import Entities.Person;

public class EntityUtils {
    /**
     * @param erfNumber
     * @return boolean
     * @throws SQLException
     * @throws IOException
     */
    public static boolean checkAddress(EntityManager entityManager, int erfNumber) throws SQLException, IOException {
        String address = entityManager.findAddress(erfNumber);
        if (address != null) {
            return true;
        }
        System.out.println("Address not found in database. Address Details: ");
        boolean registration = entityManager.registerAddress(erfNumber,
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
     * @param role
     * @return Person
     * @throws IOException
     * @throws SQLException
     */
    public static Person checkPersonDetails(EntityManager entityManager, String role, Person person) throws IOException, SQLException {
        OutputUtils.printCentered(role.toUpperCase());
        int erfNumber;
        if (person != null) {
            entityManager.assignPerson(role, person);
            return person;
        }
        erfNumber = Input.expect("Address ERF Number").toInteger();

        if (!checkAddress(entityManager, erfNumber)) {
            throw new SQLDataException("No address has been linked to the given ERF number.");
        }
        person = entityManager.registerParticipant(role, entityManager.selectPerson(role).getNumber());
        return person;
    }
}

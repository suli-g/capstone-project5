package Components.DataEntry;

import Entities.Person;

/**
 * Represents a PersonEntry
 */
public class PersonEntry extends EntityEntryBuilder {
    /**
     * Parses a {@link Person} object into a {@link String}.
     * 
     * @param person the person object to parse.
     * @return a {@link String} representing a {@link Person}
     */

    public static String unparse(Person person) {
        return getInstance().append(person.getFirstName())
                .append(person.getLastName())
                .append(person.getAddress())
                .append(person.getEmailAddress())
                .append(person.getNumber())
                .toString();
    }

    /**
     * Parses a {@link String} array into a {@link Person} object.
     * 
     * @param data the array to parse.
     * @return a Person object
     * @throws NumberFormatException if the phone number of the person cannot be parsed.
     * @throws IndexOutOfBoundsException If {@code data} does not have exactly 5 values.
     */
    public static Person parse(String data) throws NumberFormatException, IndexOutOfBoundsException {
        String[] entry = data.split(VALUE_DELIMITER);
        if (entry.length != 5) {
            throw new IndexOutOfBoundsException("The entry data should have exactly 5 values.");
        }
        return new Person(entry[0], entry[1], entry[2], entry[3], Integer.parseInt(entry[4]));
    }
}
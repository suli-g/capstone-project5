package Components;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Handles application input.
 */
public class Input {
    private static String response = "";

    /**
     * Queries the user for an optional response.
     * 
     * @param information The information to query for.
     * @return instance of this Input object.
     * @throws NoSuchElementException if execution is terminated unexpectedly.
     */
    public static Input query(String information) throws IOException {
        System.out.print(information + "(optional): ");
        response = readInput();
        return getInstance(source);
    }

    /**
     * Queries the user for required information.
     * 
     * @param information The information to query for.
     * @return instance of this Input object.
     * @throws IllegalStateException  if input is an empty string.
     * @throws NoSuchElementException if execution is terminated unexpectedly.
     */
    public static Input expect(String information) throws IllegalStateException, IOException {
        System.out.print(information + ": ");
        response = readInput();
        if (response == "") {
            throw new IllegalStateException("This information is required.");
        }
        return getInstance(source);
    }

    /**
     * @return the {@code response} as an integer.
     * @throws NumberFormatException if {@code response} cannot be converted.
     */
    public Integer toInteger() throws NumberFormatException {
        return Integer.parseInt(response);
    }

    /**
     * @param digits required in the response number.
     * @return the {@code response} as an integer.
     * @throws NumberFormatException if {@code response} cannot be parsed to an
     *                               {@link Integer}.
     */
    public Integer toInteger(int digits) throws NumberFormatException {
        int result = Integer.parseInt(response);
        if ((int) Math.log10(result) + 1 != digits) {
            throw new IllegalArgumentException("The number should be " + digits + " digits long.");
        }
        return result;
    }

    /**
     * @return {@code true} if {@code response == "yes"}, otherwise {@code false}.
     */
    public Boolean toBoolean() {
        if (response.equalsIgnoreCase("n")) {
            return false;
        }
        return true;
    }

    /**
     * @return {@code response} as a {@link Double}.
     * @throws NumberFormatException if {@code response} cannot be parsed to
     *                               {@link Double}.
     */
    public Double toDouble() throws NumberFormatException {
        return Double.parseDouble(response);
    }

    /**
     * Returns the last user response given.
     * 
     * @return the response given to {@link #expect(String)} or
     *         {@link #query(String)}.
     */
    @Override
    public String toString() {
        return response;
    }

    /**
     * Returns the last user response given if it matches the given regular
     * expression.
     * 
     * @param regex the regular expression to match the response to.
     * @return the response given to {@link #expect(String)} or
     *         {@link #query(String)}.
     * @throws IllegalStateException if {@code response} does not match
     *                               {@code regex}.
     */
    public String toString(String regex) throws IllegalStateException {
        if (!response.matches(regex)) {
            throw new IllegalStateException(String.format("The response does not match the given format (%s).", regex));
        }
        return response;
    }

    private static Input inputInstance;
    private static BufferedReader source;

    /**
     * Instantiates a new Input object if no Input objects are in scope.
     * 
     * @return Input object.
     */
    public static Input getInstance(BufferedReader reader) {
        if (inputInstance == null) {
            source = reader;
            inputInstance = new Input();
        }
        return inputInstance;
    }

    
    /** 
     * Removes additional whitespace from input if the stream for the {@link BufferedReader} for this Input instance has not ended.
     * 
     * @return the user input
     * @throws IOException if an I/O error occurs
     */
    private static String readInput() throws IOException {
        String input = source.readLine();
        if (input != null) {
            return input.trim();
        }
        return null;
    }

    private Input() {
    }
}
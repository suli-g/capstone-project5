package Components;

import java.io.BufferedReader;
import java.io.IOException;

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
     * @throws IOException if an I/O error occurs.
     */
    public static Input query(String information) throws IOException {
        System.out.print(information + "(optional): ");
        response = readInput();
        return getInstance(reader);
    }

    /**
     * Queries the user for required information.
     * 
     * @param information The information to query for.
     * @return instance of this Input object.
     * @throws IllegalStateException  if input is an empty string.
     * @throws IOException if an I/O error occurs.
     */
    public static Input expect(String information) throws IllegalStateException, IOException {
        System.out.print(information + ": ");
        response = readInput();
        if (response == "") {
            throw new IllegalStateException("This information is required.");
        }
        return getInstance(reader);
    }

    /**
     * Converts the user's response into an {@link Integer}.
     * 
     * @return the {@code response} as an integer.
     * @throws NumberFormatException if {@code response} cannot be converted.
     */
    public Integer toInteger() throws NumberFormatException {
        return Integer.parseInt(response);
    }

    /**
     * Converts the user's response to a boolean, such that "yes" is {@code true}, and any other response is considered {@code false}.
     * 
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
     * @throws NumberFormatException if the user's response cannot be converted.
     */
    public Double toDouble() throws NumberFormatException {
        return Double.parseDouble(response);
    }

    /**
     * Gets the last user response received.
     * 
     * @return the response given to {@link #expect(String)} or
     *         {@link #query(String)}.
     */
    @Override
    public String toString() {
        return response;
    }

    /**
     * Gets the last user response given if the response matches the given regular
     * expression.
     * 
     * @param regex the regular expression to match the response to.
     * @param explanation the explanation to use if the input does not match {@code regex}.
     * @return the response given to {@link #expect(String)} or
     *         {@link #query(String)}.
     * @throws IllegalStateException if {@code response} does not match
     *                               {@code regex}.
     */
    public String toString(String regex, String explanation) throws IllegalStateException {
        if (!response.matches(regex)) {
            throw new IllegalStateException(explanation);
        }
        return response;
    }

    /**
     * The single instance of {@link Input} in this java application.
     */
    private static Input inputInstance;
    /**
     * The object used to read user input.
     */
    private static BufferedReader reader;

    /**
     * Instantiates a new Input object if no Input objects are in scope.
     * 
     * @param reader the reader of the user's responses.
     * @return Input object.
     */
    public static Input getInstance(BufferedReader reader) {
        if (inputInstance == null) {
            Input.reader = reader;
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
        String input = reader.readLine();
        if (input != null) {
            return input.trim();
        }
        return null;
    }

    /**
     * This class's constructor.
     */
    private Input() {
    }
}
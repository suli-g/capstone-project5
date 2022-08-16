package IO;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Handles application input.
 */
public class Input {
    private static Input inputInstance;
    private static String response ="";
    private static Scanner source = new Scanner(System.in);

    private Input(){
    }

    /**
     * @param scanner the scanner to use as a source.
     */
    public static void setSource(Scanner scanner) {
        source = scanner;
    }

    /**
     * Instantiates a new Input object if no Input objects are in scope.
     * 
     * @return Input object.
     */
    public static Input getInstance() {
        if (inputInstance == null) {
            inputInstance = new Input();
        }
        return inputInstance;
    }

    /**
     * Queries the user for an optional response.
     * 
     * @param information The information to query for.
     * @return instance of this Input object.
     * @throws NoSuchElementException if execution is terminated unexpectedly.
     */
    public static Input query(String information) throws NoSuchElementException {
        System.out.print(information + "(optional): ");
        response = source.nextLine().trim();
        return getInstance();
    }

    /**
     * Queries the user for required information.
     * 
     * @param information The information to query for.
     * @return instance of this Input object.
     * @throws IllegalStateException if input is an empty string.
     * @throws NoSuchElementException if execution is terminated unexpectedly.
     */
    public static Input expect(String information) throws IllegalStateException, NoSuchElementException {
            System.out.print(information + ": ");
            response = source.nextLine().trim();
            if (response == "") {
                throw new IllegalStateException("This information is required.");
            }
        return getInstance();
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
     * @throws NumberFormatException if {@code response} cannot be parsed to an {@link Integer}.
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
    public Boolean toBoolean(){
        if (response.equalsIgnoreCase("n")) {
            return false;
        }
        return true;
    }

    /**
     * @return {@code response} as a {@link Double}.
     * @throws NumberFormatException if {@code response} cannot be parsed to {@link Double}.
     */
    public Double toDouble() throws NumberFormatException {
        return Double.parseDouble(response);
    }
    /**
     * Returns the last user response given.
     * @return the response given to {@link #expect(String)} or {@link #query(String)}.
     */
    @Override
    public String toString() {
        return response;
    }
}
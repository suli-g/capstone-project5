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
        System.out.print(information + "(leave blank to skip): ");
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
     * @return the previous response as an integer.
     * @throws NumberFormatException if {@code response} cannot be converted.
     */
    public Integer toInteger() throws NumberFormatException {
        return Integer.parseInt(response);
    }
    
    /**
     * @return the previous response as a double.
     * @throws NumberFormatException if {@code response} cannot be converted.
     */
    public Double toDouble() throws NumberFormatException {
        return Double.parseDouble(response);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return response;
    }
}
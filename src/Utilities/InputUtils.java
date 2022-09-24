package Utilities;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Components.Input;

/**
 * Contains various utility functions to be used when getting input from a user.
 */
public class InputUtils {
    /** 
     * Prompts the user to input a date using {@link Input#expect(String)} if {@code initialValue == null}.
     * 
     * @param initialValue the initial date to use
     * @return the date if valid
     * @throws IOException if an I/O error occurs
     * @throws DateTimeException if {@code date} is not a valid date
     * @throws IllegalStateException if no input is provided when prompted
     */
    public static String getDate(String initialValue) throws IllegalStateException, IOException, DateTimeException {
        int year, month, day;
        if (initialValue == null) {
            year = Input.expect("Year").toInteger();
            month = Input.expect("Month (1-12)").toInteger();
            day = Input.expect("Day of Month").toInteger();
            initialValue = String.format("%d-%d-%d", year, month, day);
        }
        LocalDate.parse(initialValue);
        return initialValue;
    }

    /** 
     * Prints <strong>ALL</strong> elements in the {@code options} {@link ArrayList} to the console, 
     * and prompts the user to enter an index to select.
     * 
     * @param options the options from which to choose
     * @return the element at the chosen index
     * @throws IndexOutOfBoundsException if the user enters an invalid index
     * @throws IllegalStateException if no input is provided when prompted
     * @throws NumberFormatException if the value provided is not a valid integer
     * @throws IOException if an I/O error occurs
     */
    public static String selectFromList(List<String> options) throws IllegalStateException, NumberFormatException, IOException{
        int totalOptions = options.size(), selection;
        for (int i = 0; i < totalOptions; i++) {
            System.out.println(options.get(i));
        }
        selection = Input.expect(">").toInteger();
        if (selection >= totalOptions) {
            throw new IllegalStateException("This information is required.");
        }
        return options.get(selection);
    }

    /** 
     * Prompts the user to enter a number if {@code initialValue} is null.
     * 
     * @param initialValue the initial value to use
     * @param promptMessage the message to use for the prompt
     * @return {@code initialValue} or the user's response as a double
     * @throws IOException if an I/O error occurs
     * @throws IllegalStateException if no input is provided when prompted
     * @throws NumberFormatException if the value provided is not a valid double
     */
    public static double getDouble(String initialValue, String promptMessage) throws IllegalStateException, IOException, NumberFormatException{
        if (initialValue != null) {
            return Double.parseDouble(initialValue);
        } else {
            return Input.expect(promptMessage).toDouble();
        }
    }

    /** 
     * Prompts the user to enter a number if {@code initialValue} is null.
     * 
     * @param initialValue the initial value to use
     * @param promptMessage the message to use for the prompt
     * @return {@code initialValue} or the user's response as a integer
     * @throws IOException if an I/O error occurs
     * @throws IllegalStateException if no input is provided when prompted
     * @throws NumberFormatException if the value provided is not a valid integer
     */
    public static int getInteger(String initialValue, String promptMessage) throws IllegalStateException, IOException, NumberFormatException{
        System.out.println(initialValue);
        if (initialValue != null) {
            return Integer.parseInt(initialValue);
        } else {
            return Input.expect(promptMessage).toInteger();
        }
    }

    /** 
    /** 
     * Prompts the user to enter a String if {@code initialValue} is null.
     * 
     * @param initialValue the initial value to use
     * @param promptMessage the message to use for the prompt
     * @return {@code initialValue} or the user's response as a String
     * @throws IOException if an I/O error occurs
     * @throws IllegalStateException if no input is provided when prompted
     */
    public static String getString(String initialValue, String promptMessage) throws IllegalStateException, IOException{
        if (initialValue != null) {
            return initialValue;
        } else {
            return Input.expect(promptMessage).toString();
        }
    }
}
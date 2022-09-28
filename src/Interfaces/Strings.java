package Interfaces;

/**
 * Contains constant values which are used internally by the application. 
 */
public interface Strings {
    /**
     * The name of the company using this application.
     */
    String COMPANY_NAME = "POISE";
    /**
     * The type of this application.
     */
    String APPLICATION_TYPE = "Project Management System";
    /**
     * The name of the file storing the database connection details.
     */
    String DB_PROPERTIES_FILE = "database.env";
    /**
     * The String to use for double-line borders.
     */
    String DOUBLE_LINE = "=";
    /**
     * The String to use for single-line borders.
     */
    String SINGLE_LINE = "-";
    /**
     * The maximum character amount to use for a line in output.
     */
    int TERMINAL_WIDTH = 100;
    /**
     * The message to show when SIGINT is received.
     */
    String USER_FORCEFULLY_ABORTED_MESSAGE = "Program execution has been forcefully stopped - Data may have been lost.";
    /**
     * The message to display when a database error occurs.
     */
    String DB_ERROR_MESSAGE = "An error occurred while accessing the database.";
    /**
     * The message to display when a database error occurs because the database configuration is incorrect.
     */
    String DB_CONFIG_ERROR_MESSAGE = "An error occurred while reading from the db.properties file.";
    /**
     * The message to display when a database error occurs because the database has been setup incorrectly.
     */
    String DB_INIT_ERROR_MESSAGE = """
        The Database failed to load or has not been setup properly.
        Run 'source setup.sql' in the 'sql' directory from the mysql cli first.
        """;
    /**
     * The message to display when an error occurs while reading user input.
     */
    String INPUT_ERROR_MESSAGE = "An error occurred while while reading from input.";
    /**
     * The message to display when the user enters a non-number value where a number is expected.
     */
    String NUMBER_EXPECTED_MESSAGE = "A number is expected here.";
    /**
     * The message to display when program execution ends naturally.
     */
    String GOOD_BYE_MESSAGE = "Good bye!";
    /**
     * The message to display when a date entered by the user is invalid.
     */
    String INVALID_DATE_MESSAGE = "The date entered was invalid.";
}

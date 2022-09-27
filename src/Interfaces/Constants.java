package Interfaces;

/**
 * Contains constant values which are used internally by the application. 
 */
public interface Constants {
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
}

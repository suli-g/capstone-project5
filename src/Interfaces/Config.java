package Interfaces;

public interface Config extends Queries {
    /**
     * The template used for the url to establish the Database connection.
     */
    String DATABASE_URL_TEMPLATE = "jdbc:mysql://%s:%d/%s?useSSL=%s";
    /**
     * The name of the file storing the database connection details.
     */
    String DB_PROPERTIES_FILE = "database.env";
    /**
     * The name of the main menu in the application.
     */
    String MAIN_MENU_NAME = "Main Menu";
    String PROJECT_MENU_NAME = "Project Menu";
    String PARTICIPANT_MENU_NAME = "Participant Menu";
    String ACCOUNT_MENU_NAME = "Account Menu";
    String PERSON_MENU_NAME = "Person Menu";
    String PROGRESS_MENU_NAME = "Progress Menu";
    /**
     * The maximum length to use for console messages
     */
    int MAX_LENGTH = 80;
    /**
     * The character to use at nodes (corners) in the frame created.
     */
    char NODE_CHAR = '+',
            /**
             * The character to use for edges (lines) in the frame created.
             */
            EDGE_CHAR = '-';
    /**
     * The string to be used if a value is NULL.
     */
    String NOT_SET = "N/A";
    /**
     * The name of the company
     */
    String COMPANY_NAME = "POISE";
    /**
     * The type of this application.
     */
    String APPLICATION_TYPE = "Project Management System";
    /**
     * The option shown to users to quit the application.
     */
    String QUIT_COMMAND = "quit", QUIT_COMMAND_DESCRIPTION = "quit the application";
    /**
     * The option shown to users to move back to a previous menu.
     */
    String BACK_COMMAND = "back", BACK_COMMAND_DESCRIPTION = "go back to previous menu";
}

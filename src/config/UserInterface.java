package config;

public interface UserInterface extends Menus {
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
    String COMPANY_NAME = "POISED";
    String QUIT_COMMAND = "quit", QUIT_COMMAND_DESCRIPTION = "quit the application";
    String BACK_COMMAND = "back", BACK_COMMAND_DESCRIPTION = "go back to previous menu";
}

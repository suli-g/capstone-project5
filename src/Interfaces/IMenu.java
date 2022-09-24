package Interfaces;

import Components.Menu.Menu;

/**
 * Contains String constants to be used for the Menus in the application.
 * Can be modified for accessibility purposes (translation).
 */
public interface IMenu {
    /**
     * The names the menus used in this application.
     */
    String MAIN_MENU_NAME = "Main Menu",
            PROJECT_MENU_NAME = "Project Menu",
            PARTICIPANT_MENU_NAME = "Participant Menu",
            ACCOUNT_MENU_NAME = "Account Menu",
            PERSON_MENU_NAME = "Person Menu",
            PROGRESS_MENU_NAME = "Progress Menu";
    /**
     * The option shown to users when quitting the application.
     */
    String QUIT_COMMAND = "quit";
    /**
     * The description shown to the user for the {@link #QUIT_COMMAND}.
     */
    String QUIT_COMMAND_DESCRIPTION = "quit the application";
    /**
     * The option shown to users to move back to a previous menu.
     */
    String BACK_COMMAND = "back";
    /**
     * The description shown to the user for the {@link #BACK_COMMAND}.
     */
    String BACK_COMMAND_DESCRIPTION = "go back to previous menu";
    /**
     * Shown as the first menu when the application is started.
     */
    Menu MAIN_MENU = new Menu(MAIN_MENU_NAME) {
        {
            put("show", "show all projects");
            put("show -i", "show incomplete projects");
            put("show -f", "show finalized projects");
            put("show -o", "show outstanding projects");
            put("create", "Create new project");
            put("select", "Select a project");
        }
    };
    /**
     * Shown when a project is selected.
     */
    Menu PROJECT_MENU = new Menu(PROJECT_MENU_NAME) {
        {
            put("view", "View current project details");
            put("participants", "Show a list of participants in this project");
            put("account", "Modify the cost and amount paid of the project");
            put("fix", "Enter details for missing participants");
            put("progress", "Change due date to the given date (YYYY-MM-dd)");
        }
    };
    /**
     * Shown when a project's account is queried.
     */
    Menu ACCOUNT_MENU = new Menu(ACCOUNT_MENU_NAME) {
        {
            put("due", "Change the amount due for the project.");
            put("paid", "Change the amount paid for the project.");
        }
    };
    /**
     * Shown when a project's participants has been queried.
     */
    Menu PARTICIPANT_MENU = new Menu(PARTICIPANT_MENU_NAME) {
        {
            put("list", "List all participants involved in this project.");
            put("select", "Select a participant to edit");
        }
    };
    /**
     * Shown when a Person's individual details are queried.
     */
    Menu PERSON_MENU = new Menu(PERSON_MENU_NAME) {
        {
            put("view", "View the details for the currently selected participant");
            put("phone", "Change selected participant's phone number");
            put("email", "Change selected participant's email address");
        }
    };
    /**
     * Shown when a project's progress has been queried.
     */
    Menu PROGRESS_MENU = new Menu(PROGRESS_MENU_NAME) {
        {
            // This should be updated with Config.DATE_SEGMENT_SEPARATOR to avoid confusion.
            put("due", "Edit the date due");
            put("due {date}", "Set the date due to {date} (format: YYYY-MM-dd)");
            put("finalize", "Set current date as date finalized.");
        }
    };
}

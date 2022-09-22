package Interfaces;

import Components.Menu.Menu;

public interface Menus extends Overviews {
    Menu MAIN_MENU = new Menu(MAIN_MENU_NAME) {
        {
            put("show", "show all projects");
            put("show -i", "show incomplete projects");
            put("show -f", "show finalized projects");
            put("show -o", "show outstanding projects");
            put("create", "Create new project");
            put("select", "Select a project");
        }
    },
            PROJECT_MENU = new Menu(PROJECT_MENU_NAME) {
                {
                    put("view", "View current project details");
                    put("participants", "Show a list of participants in this project");
                    put("account", "Modify the cost and amount paid of the project");
                    put("fix", "Enter details for missing participants");
                    put("progress", "Change due date to the given date (YYYY-MM-dd)");
                }
            },
            ACCOUNT_MENU = new Menu(ACCOUNT_MENU_NAME) {
                {
                    put("due", "Change the amount due for the project.");
                    put("paid", "Change the amount paid for the project.");
                }
            },
            PARTICIPANT_MENU = new Menu(PARTICIPANT_MENU_NAME) {
                {
                    put("list", "List all participants involved in this project.");
                    put("select", "Select a participant to edit");
                }
            },
            PERSON_MENU = new Menu(PERSON_MENU_NAME) {
                {
                    put("view", "View the details for the currently selected participant");
                    put("phone", "Change selected participant's phone number");
                    put("email", "Change selected participant's email address");
                }
            },

            PROGRESS_MENU = new Menu(PROGRESS_MENU_NAME) {
                {
                    // This should be updated with Config.DATE_SEGMENT_SEPARATOR to avoid confusion.
                    put("due", "Edit the date due");
                    put("due {date}", "Set the date due to {date} (format: YYYY-MM-dd)");
                    put("finalize", "Set current date as date finalized.");
                }
            };
}

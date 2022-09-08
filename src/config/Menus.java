package config;

import Components.Menu.Menu;

public interface Menus extends Config {
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

    Menu PROJECT_MENU = new Menu(PROJECT_MENU_NAME) {
        {
            put("due", "Change due date");
            put("paid", "Change amount paid");
            put("participants", "Show a list of participants in this project");
            put("fix", "Enter details for missing participants");
            put("finalize", "Finalize the project");
        }
    };

    Menu PERSONNEL_MENU = new Menu(PERSON_MENU_NAME) {
        {
            put("email", "Change Email Address");
            put("phone", "Change Phone Number");
        }
    };
}

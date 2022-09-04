package config;

import Components.Menu;

public interface Menus {
    Menu MAIN_MENU = new Menu("Main") {
        {
            put("show", "show all projects");
            put("show -i", "show incomplete projects");
            put("show -f", "show finalized projects");
            put("show -o", "show outstanding projects");
            put("create", "Create new project");
            put("select", "Select a project");
        }
    };

    Menu PROJECT_MENU = new Menu("Project") {
        {
            put("due", "Change due date");
            put("paid", "Change amount paid");
            put("participants", "Show a list of participants in this project");
            put("fix missing", "Enter details for missing participants");
            put("finalize", "Finalize the project");
        }
    };

    Menu PERSONNEL_MENU = new Menu("Personnel") {
        {
            put("email", "Change Email Address");
            put("phone", "Change Phone Number");
        }
    };
}

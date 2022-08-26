package Components;

import Components.DataEntry.DelimitedValueString;

public interface UserInterface extends DelimitedValueString {
    /**
     * The maximum length to use for console messages
     */
    int MAX_LENGTH = 80;
    /**
     * The character to use at nodes (corners) in the frame created.
     */
    char NODE_CHAR = '+';
    /**
     * The character to use for edges (lines) in the frame created.
     */
    char EDGE_CHAR = '-';
    String NOT_SET = "N/A";
    String DATABASE_URL_TEMPLATE = "jdbc:mysql://%s:%d/%s?useSSL=%s";
    String COMPANY_NAME = "POISED",
            ENTITY_DATA_DIRECTORY = "data";
    Menu MAIN_MENU = new Menu("Project") {
        {
            put("list", "List all projects");
            put("list -i", "List incomplete projects");
            put("list -o", "List outstanding projects");
            put("create", "Create new project");
            put("select", "Select a project");
            put("quit", "Quit application");
        }
    };

    Menu PROJECT_MENU = new Menu("Project") {
        {
            put("due", "Change due date");
            put("paid", "Change amount paid");
            put("contractor", "Change a contractor's details");
            put("fix missing", "Enter details for missing participants");
            put("finalize", "Finalize the project");
            put("back", "Back to Main Menu");
            put("quit", "Quit application");
        }
    };

    Menu PERSONNEL_MENU = new Menu("Personnel") {
        {
            put("email", "Change Email Address");
            put("phone", "Change Phone Number");
            put("back", "Back to Main Menu");
            put("quit", "Quit Application");
        }
    };

    String OVERVIEW_HEADER_FORMAT = """
            === Project Details ===
                project number:             %-8d
                project name:               %-8s
                project cost:               R%-7.2f
                total paid to date:         R%-7.2f
            """;
    String OVERVIEW_FOOTER_FORMAT = """
            === Completion Status ===
                Completion status:          %-8s
                Date finalized:             %-8s
            """;
    String PERSON_OVERVIEW_FORMAT = """
            === %s ===
                Name:                       %-8s
                Phone Number:               %-8s
            """;
}

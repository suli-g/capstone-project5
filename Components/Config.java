package Components;
import Factories.EntityFactory;

public abstract class Config extends EntityFactory {
    protected static final Menu PROJECT_MENU = new Menu("project"){{
        put("d", "Change due date");
        put("p", "Change amount paid");
        put("e", "Change a contractor's details");
        put("f", "Finalize the project");
        put("q", "Quit application");
    }};

    protected static final Menu PERSONNEL_MENU = new Menu("Personnel"){{
        put("e", "Change Email Address");
        put("p", "Change Phone Number");
        put("b", "Back to main Menu");
        put("q", "Quit Application");
    }};

    protected static final Menu MAIN_MENU = new Menu("Main"){{
        put("a", "List all Projects");
        put("i", "List all incomplete Projects");
        put("c", "List all completed Projects");
        put("o", "List all overdue Projects");
        put("l", "List all Projects");
        put("q", "Quit application");
    }};
}
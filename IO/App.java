package IO;

import java.util.Stack;

import Components.Config;
import Components.Menu;
import Entities.Entity;
import Entities.Person;
import Entities.Project;

public class App extends Config {
    private static App appInstance;
    private static Entity selectedEntity;
    private static Stack<Menu> menuStack;
    public static App getInstance() {
        if (appInstance == null) {
            appInstance = new App();
        }
        return appInstance;
    }

    public Menu selectFromMenu() {
        Menu currentMenu;
        String selectedOption;
        if (selectedEntity instanceof Person) {
            currentMenu = PERSONNEL_MENU;
        } else if (selectedEntity instanceof Project) {
            currentMenu = PROJECT_MENU;
        } else {
            currentMenu = MAIN_MENU;
        }
        System.out.println(currentMenu);
        selectedOption = Input.expect("").toString();
        return currentMenu;
    }

    public void selectEntity(Entity entity) {
        selectedEntity = entity;
    }
}

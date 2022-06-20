package Components;

import java.util.HashMap;

/**
 * Represents a Menu.
 */
public class Menu {
    private HashMap<String, String> descriptions;
    private String name;

    /**
     * Creates a menu with the given name and options.
     * 
     * @param menuName the name of the menu.
     * @param menuOptionDescriptions a map of options and their descriptions.
     */
    public Menu(String menuName, HashMap<String, String> menuOptionDescriptions) {
        name = menuName;
        descriptions = menuOptionDescriptions;
    }
    
    @Override
    public String toString() {
        StringBuilder menuDescription = new StringBuilder(name)
            .append(" Menu:\n")
            .append("[Option] Description");
        descriptions.forEach((option, description) -> {
            menuDescription.append('\n').append("[").append(option).append("]\t").append(description);
        });
        return menuDescription.toString();
    }
}

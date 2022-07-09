package Components;

import java.util.HashMap;

/**
 * Represents a Menu.
 */
public class Menu extends HashMap<String, String> {
    private static HashMap<String, Menu> menuInstances;
    private String title;
    /**
     * Creates a menu with the given name and options.
     * 
     * @param menuName the name of the menu.
     * @param menuOptionDescriptions a map of options and their descriptions.
     */
    public Menu(String menuName) {
        title = menuName;
    }
    
    public static Menu getMenu(String title) {
        if (menuInstances == null) {
            menuInstances = new HashMap<>();
        }
        if (menuInstances.get(title) == null) {
            menuInstances.put(title, new Menu(title));
        }
        return menuInstances.get(title);
    }

    @Override
    public String toString() {
        StringBuilder menuDescription = new StringBuilder(title)
            .append(" Menu:");
        this.forEach((option, description) -> {
            menuDescription.append('\n').append("").append(option).append(":\t").append(description);
        });
        return menuDescription.toString();
    }
}

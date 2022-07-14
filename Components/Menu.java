package Components;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Represents a Menu.
 */
public class Menu extends LinkedHashMap<String, String> {
    private static HashMap<String, Menu> menuInstances;
    private static String borderMarker = "-";
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
    
    /**
     * Gets a menu instance.
     * 
     * @param menuTitle the title of the menu returned.
     * @return the menu with {@code title == menuTitle}, else creates a new menu instance.
     */
    public static Menu getMenu(String menuTitle) {
        if (menuInstances == null) {
            menuInstances = new HashMap<>();
        }
        if (menuInstances.get(menuTitle) == null) {
            menuInstances.put(menuTitle, new Menu(menuTitle));
        }
        return menuInstances.get(menuTitle);
    }

    /**
     * Render this menu into a logical menu-type format.
     * 
     * @return this menu's details.
     */
    @Override
    public String toString() {
        int titleLength = title.length() + 6;
        // Align the borders with title
        String optionFormat = "\n%-"+ titleLength + "s%-16s";
        StringBuilder menuDescription = new StringBuilder(borderMarker.repeat(titleLength))
            .append('\n')
            .append(title)
            .append(" Menu:");
            this.forEach((option, description) -> {
                menuDescription
                .append(String.format(optionFormat, option, description));
            });
            return menuDescription
            .append("\n")
            .append(borderMarker.repeat(titleLength))
            .append("\n")
            .toString();
    }
}

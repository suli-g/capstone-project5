package Components.Menu;

import java.util.LinkedHashMap;

/**
 * Represents a Menu.
 */
public class Menu extends LinkedHashMap<String, String> {
    /**
     * The name of this Menu instance.
     */
    private String name;
    public String getName() {
        return name;
    }

    /**
     * Creates a menu with the given name and options.
     * 
     * @param menuName the name of the menu instance.
     */
    public Menu(String menuName) {
        name = menuName;
    }

    /**
     * Render this menu into a logical menu-type format.
     * 
     * @return this menu's details.
     */
    @Override
    public String toString() {
        StringBuilder menuDescription = new StringBuilder();
            this.forEach((option, description) -> {
                menuDescription
                .append(Option.parse(option, description));
            });
            
            return menuDescription
            .toString();
    }
}

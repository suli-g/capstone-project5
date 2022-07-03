package Components.Menu;

import java.util.HashMap;
import Entities.Entity;

/**
 * Represents a Menu.
 */
public class Menu extends HashMap<String, Option> {
    private String name;
    /**
     * Creates a menu with the given name and options.
     * 
     * @param menuName the name of the menu.
     * @param menuOptionDescriptions a map of options and their descriptions.
     */
    public Menu(String menuName) {
        name = menuName;
    }

    public Option put(String trigger, String description) {
        Option newOption = new Option(description);
        super.put(trigger, newOption);
        return newOption;
    }

    public void run(String trigger, Entity target) {
        if (this.containsKey(trigger)) {
            this.get(trigger).run(target);
        } else {
            System.out.println("The option: '" + trigger + "' is invalid.");
        }
    }
    
    public void run(String trigger) {
        if (this.containsKey(trigger)) {
            this.get(trigger).run();
        } else {
            System.out.println("The option: '" + trigger + "' is invalid.");
        }
        
    }

    @Override
    public String toString() {
        StringBuilder menuDescription = new StringBuilder(name)
            .append(" Menu:\n")
            .append("[Option] Description");
        this.forEach((trigger, option) -> {
            String formattedTrigger = "[" + trigger + "]";
            menuDescription
            .append(String.format("\n%-6s%s", formattedTrigger, option));
        });
        return menuDescription.toString();
    }
}

package Components;

import java.util.ArrayList;
import java.util.HashMap;

import Entities.Entity;

interface MenuAction {
    public void run(Entity entity);
    public void run();
}

class Option implements MenuAction {
    String title, description;
    MenuAction action;
    public Option(String description, MenuAction action) {
        this(description);
        this.action = action;
    }

    public Option(String description) {
        this.description = description;
    }
    
    public void setAction(MenuAction action) {
        this.action = action;
    }

    @Override
    public void run(Entity entity) {
        if (action == null) {
            System.out.println("TODO: Something");
        } else {
            action.run(entity);
        }
    }

    @Override
    public void run() {
        if (action == null) {
            System.out.println("TODO: Something");
        } else {
            action.run();
        }
    }

    @Override
    public String toString() {
        return description;
    }
}

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

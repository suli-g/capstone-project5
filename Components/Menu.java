package Components;

import java.util.HashMap;

import Entities.Entity;

interface MenuAction {
    void run(Entity entity);
}

public class Menu {
    private HashMap<String, MenuAction> options;
    private HashMap<String, String> descriptions;
    public int length = 0;

    public Menu(HashMap<String, String> menuDescriptions) {
        descriptions = menuDescriptions;
    }

    public void pick(String option, Entity entity) {
        if (options.containsKey(option)) {
            options.get(option).run(entity);
        } else {
            throw new IllegalArgumentException("The '" + option + "' option does not exist on this menu.");
        }
    }

    public void setAction(String option, MenuAction action) {
        options.put(option, action);
        length++;
    }

    @Override
    public String toString() {
        StringBuilder menuDescription = new StringBuilder("Option:      Description");
        descriptions.forEach((option, description) -> {
            menuDescription.append(option).append(":\t\t").append(description).append('\n');
        });
        menuDescription.deleteCharAt(length - 1);
        return menuDescription.toString();
    }
}

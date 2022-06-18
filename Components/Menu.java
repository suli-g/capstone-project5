package Components;

import java.util.HashMap;

public class Menu {
    private HashMap<String, String> descriptions;
    private String name;

    public Menu(String menuName, HashMap<String, String> menuDescriptions) {
        name = menuName;
        descriptions = menuDescriptions;
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

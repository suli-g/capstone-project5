package Components.Menu;

import java.util.LinkedHashMap;

import Components.Menu.MenuException.InvalidSelectionException;

/**
 * Represents a Menu.
 */
public class Menu extends LinkedHashMap<String, String> {
    /**
     * The name of this Menu instance.
     */
    private String name;
    private String[] selected;
    
    /** 
     * @return the name of this Menu.
     */
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
     * @return the first String in {@link #selected}.
     */
    public String getCommand() {
        return selected[0];
    }

    
    /** 
     * Gets the String at {@code index} in {@link #selected}.
     * 
     * @param index the index of the String to get.
     * @return the String at {@code index}; 
     *      null if {@code index} is out of bounds.
     * 
     */
    public String getParameter(int index){
        if (index >= selected.length - 1) {
            return null;
        }
        return selected[index + 1];
    }

    
    /** 
     * Gets an array of the Strings entered by the user.
     * 
     * @return {@link #selected}
     */
    public String[] getSelected() {
        return selected;
    }

    
    /** 
     * Sets {@link #selected} to {@code command} if {@code command} is a valid menu choice.
     * 
     * @param command an array of the strings entered by the user.
     * @return {@link #selected}.
     * @throws InvalidSelectionException if the user enters an invalid key.
     */
    public String[] setSelected(String... command) throws InvalidSelectionException {
        if (!containsKey(command[0])) {
            throw new InvalidSelectionException(command[0]);
        } else {
            selected = command;
        }
        return selected;
    }

    /**
     * Render this menu into a logical menu-type formatted {@link String}.
     * 
     * @return this menu's details.
     */
    @Override
    public String toString() {
        StringBuilder menuDescription = new StringBuilder();
            this.forEach((option, description) -> {
                menuDescription
                .append(OptionDecorator.parse(option, description));
            });
            
            return menuDescription
            .toString();
    }
}

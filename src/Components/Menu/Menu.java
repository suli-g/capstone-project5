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
    /**
     * The user's response, split into an array by spaces.
     */
    private String[] response;

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
     * @return the first String in {@link #response}.
     */
    public String getCommand() {
        if (response == null) {
            return null;
        }
        return response[0].toLowerCase();
    }

    /**
     * Gets the String at {@code index} in {@link #response}.
     * 
     * @param index the index of the String to get.
     * @return the String at {@code index};
     *         null if {@code index} is out of bounds.
     * 
     */
    public String getParameter(int index) {
        if (response.length <= index) {
            return null;
        }
        return response[index + 1].toLowerCase();
    }

    /**
     * Gets an array of the Strings entered by the user.
     * 
     * @return {@link #response}
     */
    public String[] getSelected() {
        return response;
    }

    /**
     * Sets {@link #response} to {@code command} if {@code command} is a valid menu
     * choice.
     * 
     * @param command an array of the strings entered by the user.
     * @return {@link #response}.
     * @throws InvalidSelectionException if the user enters an invalid key.
     */
    public String[] setSelected(String... command) throws InvalidSelectionException {
        if (!containsKey(command[0])) {
            throw new InvalidSelectionException(command[0]);
        } else {
            response = command;
        }
        return response;
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

    /**
     * Represents an Exception thrown when an invalid selection is made in this
     * Menu.
     */
    public static class InvalidSelectionException extends IllegalArgumentException {
        /**
         * @param option the option for which the selection is invalid.
         */
        public InvalidSelectionException(String option) {
            super(String.format("The option '%s'is invalid for this menu.", option));
        }
    }
}

package Components.Menu;

/**
 * Represents an option for a {@link Menu}.
 */
public class OptionDecorator {
    /**
     * The format of all option-strings.
     */
    private static String optionFormat;
    
    /** 
     * Formats a string as an option for a {@link Menu}.
     * 
     * @param option the option entered by the user.
     * @param description the description explaining the option's function.
     * @return an option-formatted string.
     */
    public static String parse(String option, String description) {
        return String.format(optionFormat, option, description);
    }

    /** 
     * Sets the display length for all Option strings when printed in a menu.
     * 
     * @param titleLength the length of the menu title.
     */
    public static void setSize(int titleLength) {
        optionFormat = "\n%-"+ titleLength + "s%-16s";
    }
}
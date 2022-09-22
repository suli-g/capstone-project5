package Components.Menu;

public class MenuException {
    /**
     * Represents an Exception thrown when the user enters an invalid selection.
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

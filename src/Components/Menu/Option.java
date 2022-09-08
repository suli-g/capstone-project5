package Components.Menu;

class Option {
    private static String optionFormat;
    public static String parse(String option, String description) {
        return String.format(optionFormat, option, description);
    }

    public static void setSize(int titleLength) {
        optionFormat = "\n%-"+ titleLength + "s%-16s";
    }
}
import java.util.Scanner;

interface IPrompter {
    Scanner INPUT = new Scanner(System.in);
}

public class Prompter implements IPrompter {
    private static Prompter prompterInstance;
    private static String response;

    private Prompter(){}

    public static Prompter getInstance() {
        if (prompterInstance == null) {
            prompterInstance = new Prompter();
        }
        return prompterInstance;
    }

    public static void prompt(String information) {
        System.out.print(information + "(leave blank to skip): ");
        response = INPUT.nextLine().trim();
    }

    public static void expect(String information) throws IllegalArgumentException {
        System.out.print(information + ": ");
        if (INPUT.nextLine().length() == 0) {
            throw new IllegalArgumentException("A response is expected here.");
        }
        response = INPUT.nextLine().trim();
    }

    public static String getString() {
        return response;
    }

    public static int getInt() throws NumberFormatException, NullPointerException {
        return Integer.parseInt(response);
    }

    public static double getDouble() throws NumberFormatException, NullPointerException {
        return Double.parseDouble(response);
    }
}
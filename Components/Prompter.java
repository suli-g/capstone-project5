package Components;
import java.util.Scanner;

interface IPrompter {
    Scanner INPUT = new Scanner(System.in);
}

public class Prompter implements IPrompter {
    private static Prompter prompterInstance;
    private static String response;

    private Prompter(String lastResponse){
        response = lastResponse;
    }

    private static Prompter getInstance(String lastResponse) {
        if (prompterInstance == null) {
            prompterInstance = new Prompter(lastResponse);
        }
        return prompterInstance;
    }

    public static Prompter query(String information) {
        System.out.print(information + "(leave blank to skip): ");
        response = INPUT.nextLine().trim();
        return getInstance(response);
    }

    public static Prompter expect(String information) {
        do {
            System.out.print(information + ": ");
            response = INPUT.nextLine().trim();
            if (response == "") {
                System.out.println("A response is expected here.");
            }
        }
        while (response == "");
        return getInstance(response);
    }
    
    public int toInteger() throws NumberFormatException, NullPointerException {
        return Integer.parseInt(response);
    }
    
    public double toDouble() throws NumberFormatException, NullPointerException {
        return Double.parseDouble(response);
    }
    
    @Override
    public String toString() {
        return response;
    }
}
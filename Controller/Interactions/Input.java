package Controller.Interactions;
import java.util.Scanner;

interface IPrompter {
    Scanner INPUT = new Scanner(System.in);
}

public class Input implements IPrompter {
    private static Input prompterInstance;
    private static String response;

    private Input(){}

    public static Input getInstance() {
        if (prompterInstance == null) {
            prompterInstance = new Input();
        }
        return prompterInstance;
    }

    public static Input query(String information) {
        System.out.print(information + "(leave blank to skip): ");
        response = INPUT.nextLine().trim();
        return getInstance();
    }

    public static Input expect(String information) {
        do {
            System.out.print(information + ": ");
            response = INPUT.nextLine().trim();
            if (response == "") {
                System.out.println("A response is expected here.");
            }
        }
        while (response == "");
        return getInstance();
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
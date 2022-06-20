package Builders;
import java.util.Scanner;

interface IPrompter {
    Scanner INPUT = new Scanner(System.in);
}

public class QueryBuilder implements IPrompter {
    private static QueryBuilder prompterInstance;
    private static String response;

    private QueryBuilder(String lastResponse){
        response = lastResponse;
    }

    private static QueryBuilder getInstance(String lastResponse) {
        if (prompterInstance == null) {
            prompterInstance = new QueryBuilder(lastResponse);
        }
        return prompterInstance;
    }

    public static QueryBuilder query(String information) {
        System.out.print(information + "(leave blank to skip): ");
        response = INPUT.nextLine().trim();
        return getInstance(response);
    }

    public static QueryBuilder expect(String information) {
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
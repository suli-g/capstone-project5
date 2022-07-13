package IO;

interface Console {
    int MAX_LENGTH = 80;
    char NODE_CHAR = '+';
    char EDGE_CHAR = '-';
}

/**
 * Handles application output.
 */
public class Output implements Console {
    private static Output displayInstance;

    private Output() {
    }

    /**
     * Creates a new Output instance if no Output instance is in scope.
     * 
     * @return
     */
    public static Output getInstance() {
        if (displayInstance == null) {
            displayInstance = new Output();
        }
        return displayInstance;
    }

    /**
     * Prints a border surrounding the given {@code text}.
     * 
     * @param edgeChar the character used in border lines.
     * @param nodeChar the character used in corners.
     * @param text the text in the border.
     */
    public static void printBorder(char edgeChar, char nodeChar, String text) {
        int textLength = text.length(),
                gapIndex = (int) (MAX_LENGTH - text.length()) / 2;

        for (int i = 0; i < MAX_LENGTH; i++) {
            if (i == 0) {
                System.out.print(nodeChar);
            } else if (i == gapIndex) {
                System.out.print(text);
                i += textLength;
            } else if (i == MAX_LENGTH - 1) {
                System.out.println(nodeChar);
            } else {
                System.out.print(edgeChar);
            }
        }
    }

    /**
     * Prints a header for the company.
     */
    public static void printHeader(String companyName) {
        String logoText = String.format("{ %s }", companyName),
                headingText = "Project Management System";

        printBorder(NODE_CHAR, EDGE_CHAR, logoText);
        printBorder(' ', ':', headingText);
        printBorder(NODE_CHAR, EDGE_CHAR, "");
    }
}
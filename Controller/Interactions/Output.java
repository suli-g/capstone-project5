package Controller.Interactions;

public class Output {
    private static Output displayInstance;

    private Output() {
    }

    public static Output getInstance() {
        if (displayInstance == null) {
            displayInstance = new Output();
        }
        return displayInstance;
    }

    public static String drawHorizontalLine(char lineCharacter, int length) {
        return String.valueOf(lineCharacter).repeat(length);
    }

    public static String embedTextInLine(String content, String base ) {
        int baseLength = base.length(),
            textLength = content.length(),
            impressionLength = baseLength - textLength;

        if (impressionLength % 2 != 0) {
            impressionLength -= 1;
        }
        int startIndex = impressionLength / 2;
        return new StringBuilder(base)
            .replace(startIndex, startIndex + textLength, content)
            .toString();
        
    }
}
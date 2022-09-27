package Utilities;

import java.util.ArrayList;
import java.util.Map;

import Interfaces.Constants;

/**
 * Handles application output.
 */
public class OutputUtils implements Constants {

    /**
     * Prints each element from a list out on a new line.
     * 
     * @param list the list to print.
     * @param <T> any type that overrides the {@link Object#toString()} method.
     */
    public static <T> void printList(ArrayList<T> list) {
        int i = 0, total = list.size();
        printDoubleLine();
        for (T entry : list) {
            System.out.println(entry.toString());
            if (i < total - 1) {
                printLine();
            }
            i++;
        }
    }

    /**
     * Prints each key-value pair in a {@link Map} to the terminal.
     * 
     * @param map the map to print.
     * @param <T> any type that overrides the {@link Object#toString()} method.
     */
    public static <T> void printMap(Map<String, T> map) {
        int i = 0, total = map.size();
        printDoubleLine();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            System.out.println(entry.getKey());
            printDivider("- ");
            System.out.println(entry.getValue().toString());
            if (i < total - 1) {
                printLine();
            }
            i++;
        }
    }

    /**
     * Prints a horizontal line onto the terminal.
     */
    public static void printLine() {
        printDivider(SINGLE_LINE);
    }
    
        /**
         * Prints a horizontal double-line onto the terminal.
         */
    public static void printDoubleLine() {
        printDivider(DOUBLE_LINE);
    }

    /**
     * Prints the given {@code} divider to the terminal, using no more than {@value Constants#TERMINAL_WIDTH} lines.
     * 
     * @param divider the string to be used as a divider.
     */
    public static void printDivider(String divider) {
        int dividerLength = divider.length(),
                repeatLength = (int) TERMINAL_WIDTH / dividerLength;
        System.out.println(new StringBuilder()
                .append(divider.repeat(repeatLength))
                .append(divider.substring(0, repeatLength % dividerLength))
                .toString());
    }

    /**
     * Prints the given {@code text} in the center of the terminal window.
     * 
     * @param text the text to print.
     */
    public static void printCentered(String text) {
        System.out.println(centerText(text));
    }

    /**
     * Aligns {@code text} in the center of the terminal window according to {@link Constants#TERMINAL_WIDTH}.
     * 
     * @param text the text to center.
     * @return {@code text} with whitespace-padding on either side.
     */
    public static String centerText(String text) {
        int gapSize = (int) (TERMINAL_WIDTH - text.length()) / 2;
        return new StringBuilder(" ".repeat(gapSize))
                        .append(text).append(" ".repeat(gapSize)).toString();
    }

    /**
     * Prints the given {@code text} as a warning for the user.
     * 
     * @param text the text to use as a warning.
     */
    public static void printWarning(String text) {
        System.out.println();
        printLine();
        printCentered("***" + text + "***");
        printLine();
    }

    /**
     * Prints a header.
     * 
     * @param headerText the text to use in the heading.
     */
    public static void printHeading(Object headerText) {
        printCentered(new StringBuilder("{ ").append(headerText).append(" }").toString());
    }
}
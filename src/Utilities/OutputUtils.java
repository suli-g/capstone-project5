package Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

interface Console {
    String DOUBLE_LINE = "=",
        SINGLE_LINE = "-";
    
        int MAX_LENGTH = 80;
}

/**
 * Handles application output.
 */
public class OutputUtils implements Console {
    
    /** 
     * @param list
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
     * @param map
     */
    public static <T> void printMap(HashMap<String, T> map) {
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

    public static void printLine() {
        printDivider(SINGLE_LINE);
    }

    public static void printDoubleLine() {
        printDivider(DOUBLE_LINE);
    }

    
    /** 
     * @param divider
     */
    public static void printDivider(String divider) {
        int dividerLength = divider.length(),
                repeatLength = (int) MAX_LENGTH / dividerLength;
        System.out.println(new StringBuilder()
                .append(divider.repeat(repeatLength))
                .append(divider.substring(0, repeatLength % dividerLength))
                .toString());
    }

    
    /** 
     * @param text
     */
    public static void printCentered(String text) {
        int gapSize = (int) (MAX_LENGTH - text.length()) / 2;
        System.out.println(
                new StringBuilder(" ".repeat(gapSize))
                        .append(text).append(" ".repeat(gapSize)).toString());
    }

    
    /** 
     * @param text
     */
    public static void printWarning(String text) {
        printCentered("***" + text + "***");
    }

    /**
     * Prints a header.
     * 
     * @param headerText the name of the company.
     */
    public static void printHeading(Object headerText) {
        printCentered(new StringBuilder("{ ").append(headerText).append(" }").toString());
    }
}
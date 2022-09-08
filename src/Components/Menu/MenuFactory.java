package Components.Menu;

import Components.ViewStack;
import config.UserInterface;

public class MenuFactory implements UserInterface {
    public static class InvalidSelectionException extends IllegalArgumentException {
        public InvalidSelectionException(String option) {
            super(String.format("The option '%s' does not exist on this menu.", option));
        }
    }

    private static ViewStack<Menu> stack;

    public static void setStack(ViewStack<Menu> menuStack) {
        stack = menuStack;
    }

    public static void addMenu(Menu menu) {
        if (stack.size() > 1) {
            menu.put(BACK_COMMAND, BACK_COMMAND_DESCRIPTION);
        }
        menu.put(QUIT_COMMAND, QUIT_COMMAND_DESCRIPTION);
    }

    private static String borderMarker = "-";


    public static void goBack() {
        if (stack.size() > 1) {
            stack.pop();
        }
    }

    public static void quit() {
        stack.clear();
    }

    public static Menu display() {
        Menu currentMenu = stack.peek();
        String name = currentMenu.getName();
        int titleLength = name.length() + 6;
        Option.setSize(titleLength);
        StringBuilder menuOutline = new StringBuilder(borderMarker.repeat(titleLength))
        .append('\n')
        .append(name)
        .append(" Menu:")
        .append("\n")
        .append(currentMenu)
        .append("\n")
        .append(borderMarker.repeat(titleLength))
        .append("\n");
        System.out.println(menuOutline.toString());
        return currentMenu;
    }       
}

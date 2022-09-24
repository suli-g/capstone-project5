package Controller;

import java.io.IOException;
import java.util.Stack;

import Components.Input;
import Components.Menu.Menu;
import Components.Menu.OptionDecorator;
import Interfaces.IMenu;
import Utilities.OutputUtils;

/**
 * Controls the instantiation of {@link Menu} instances for this application.
 */
public class MenuController implements IMenu {

    /**
     * @return {@code true} if the {@link #menuStack} is empty; {@code false} if {@link #menuStack} is not empty.
     */
    public boolean isDone() {
        return menuStack.empty();
    }

    /**
     * Creates a new MenuFactory instance if no instance exists in this application;
     * gets the instanceif an instance of MenuFactory already exists.
     * 
     * @return The single MenuFactory instance in this running application.
     */
    public static MenuController getInstance() {
        if (factoryInstance == null) {
            factoryInstance = new MenuController();
        }
        return factoryInstance;
    }

    /**
     * The menu stack.
     */
    private Stack<Menu> menuStack;

    /**
     * Adds a menu to {@link #menuStack}.
     * 
     * @param menu to add.
     */
    public void addMenu(Menu menu) {
        if (menuStack.size() > 0) {
            menu.put(BACK_COMMAND, BACK_COMMAND_DESCRIPTION);
        }
        menu.put(QUIT_COMMAND, QUIT_COMMAND_DESCRIPTION);
        menuStack.push(menu);
    }

    /**
     * Removes the {@link Menu} at the top of the stack if the stack has more than
     * one {@link Menu}.
     */
    public void popTop() {
        if (menuStack.size() > 0) {
            menuStack.pop();
        }
    }

    /**
     * Clears {@link #menuStack}.
     */
    public void clearStack() {
        menuStack.clear();
    }

    /**
     * @return the {@link Menu} at the top of {@link #menuStack}.
     */
    public Menu getCurrent() {
        return menuStack.peek();
    }

    /**
     * Prints the current {@link Menu} to the console and uses
     * {@link Input#expect(String)} to get a
     * response from the user.
     * 
     * @throws IllegalStateException if no response is entered by the user.
     * @return Menu
     */
    public Menu showCurrent() {
        Menu currentMenu = menuStack.peek();
        String name = currentMenu.getName();
        int titleLength = name.length() + 6;
        OptionDecorator.setSize(titleLength);
        StringBuilder menuOutline = new StringBuilder()
                .append(OutputUtils.centerText(name))
                .append("\n")
                .append(currentMenu)
                .append("\n");
        OutputUtils.printDoubleLine();
        System.out.println(menuOutline.toString());
        OutputUtils.printLine();
        try {
            currentMenu.setSelected(Input.expect(">").toString().split("\s"));
        } catch (IOException io) {
            io.printStackTrace();
        } catch (NullPointerException err) {
            System.out.println("Data entry was interrupted unexpectedly.");
        } catch (Menu.InvalidSelectionException err) {
            System.out.println(err.getLocalizedMessage());
        }
        return currentMenu;
    }

    /**
     * The MenuFactory instance in this application.
     */
    private static MenuController factoryInstance;

    /**
     * The constructor for this class.
     */
    private MenuController() {
        menuStack = new Stack<>();
    }
}

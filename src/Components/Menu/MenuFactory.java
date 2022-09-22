package Components.Menu;

import java.io.IOException;

import Components.Input;
import Components.ViewStack;
import Components.Menu.MenuException.InvalidSelectionException;
import Interfaces.Menus;

/**
 * Controls the instantiation of {@link Menu} instances for this application.
 */
public class MenuFactory implements Menus {

    /**
     * @return {@value true} if the {@link #menuStack} is empty; {@value false}
     *         otherwise.
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
    public static MenuFactory getInstance(ViewStack<Menu> menuStack) {
        if (factoryInstance == null) {
            factoryInstance = new MenuFactory(menuStack);
        }
        return factoryInstance;
    }

    /**
     * The menu stack.
     */
    private ViewStack<Menu> menuStack;

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

    private String borderMarker = "-";

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
                .append('\n')
                .append(name)
                .append("\n")
                .append(currentMenu)
                .append("\n")
                .append(borderMarker.repeat(titleLength))
                .append("\n");
        System.out.println(menuOutline.toString());
        try {
            currentMenu.setSelected(Input.expect(">").toString().split("\s"));
        } catch (IOException io) {
            io.printStackTrace();
        } catch (NullPointerException err) {
            System.out.println("Data entry was interrupted unexpectedly.");
        } catch (InvalidSelectionException err) {
            System.out.println(err.getLocalizedMessage());
        }
        return currentMenu;
    }

    /**
     * The MenuFactory instance in this application.
     */
    private static MenuFactory factoryInstance;

    /**
     * The constructor for this class.
     */
    private MenuFactory(ViewStack<Menu> menuStack) {
        this.menuStack = menuStack;
    }
}

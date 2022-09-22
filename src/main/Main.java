package main;

import java.io.BufferedReader;
/**
 * An application used to create and modify projects.
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;


import Components.Input;
import Components.ViewStack;
import Components.Menu.Menu;
import Components.Menu.MenuException;
import Components.Menu.MenuFactory;
import Controller.EntityManager;
import Controller.InteractionManager;
import Entities.Entity;
import Interfaces.Menus;
import Model.DatabaseConnection;
import Model.EntityModel.EntityModel;
import Utilities.OutputUtils;

public class Main implements Menus {
    private static ViewStack<Entity> entityStack;
    private static EntityManager entityManager;
    private static MenuFactory menuFactory;
    private static Menu currentMenu;
    private static InteractionManager interactionManager;

    /**
     * @param mainArgs
     * @throws SQLException
     */
    private static void initializeEntityManager(String[] mainArgs) {
        try {
            InputStream configFileStream;
            if (mainArgs.length == 0) {
                configFileStream = Main.class.getClassLoader().getResourceAsStream(DB_PROPERTIES_FILE);
            } else {
                configFileStream = new FileInputStream(mainArgs[0]);
            }
            entityManager = EntityManager.getInstance(
                    EntityModel.getInstance(DatabaseConnection.loadFromFile(configFileStream)));
            entityStack = new ViewStack<>();
            menuFactory = MenuFactory.getInstance(new ViewStack<>());
            menuFactory.addMenu(MAIN_MENU);
            interactionManager = InteractionManager.getInstance(entityManager, menuFactory, entityStack);
        } catch (IOException ioError) {
            ioError.printStackTrace();
            System.out.println("An error occurred while reading from the db.properties file.");
            System.out.println(ioError);
            System.exit(1);
        } catch (SQLException sqlError) {
            sqlError.printStackTrace();
            System.out.println("The Database failed to load or has not been setup properly.");
            System.out.println("Run 'source setup.sql' from the mysql cli first.");
            System.exit(1);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Input.getInstance(new BufferedReader(new InputStreamReader(System.in)));
        initializeEntityManager(args);
        OutputUtils.printHeading(COMPANY_NAME);
        OutputUtils.printCentered(APPLICATION_TYPE);
        OutputUtils.printLine();
        while (!menuFactory.isDone()) {
            currentMenu = menuFactory.showCurrent();
            try {
                handleInteraction();
            } catch (SQLException error) {
                OutputUtils.printWarning("An error occurred while accessing the database.");
            } catch (IOException error) {
                OutputUtils.printWarning("An error occurred while while reading from input.");
            } catch (MenuException.InvalidSelectionException error) {
                OutputUtils.printWarning("The value entered is not on the menu.");
            } 
            catch (NumberFormatException error) {
                OutputUtils.printWarning("Please enter a number.");
            }
            catch (IllegalStateException error) {
                OutputUtils.printWarning(error.getLocalizedMessage());
            }
        }
        System.out.println("Good bye!");
    }

    /**
     * Handles all user interactions.
     * 
     * @throws SQLException if a database error occurs.
     * @throws IOException if an I/O error occurs.
     */
    private static void handleInteraction() throws SQLException, IOException {
        String command = currentMenu.getCommand();
        switch (command) {
            case MenuFactory.BACK_COMMAND:
                menuFactory.popTop();
                return;
            case MenuFactory.QUIT_COMMAND:
                menuFactory.clearStack();
                return;
        }

        switch (currentMenu.getName()) {
            case MAIN_MENU_NAME:
                interactionManager.mainMenuInteraction();
                break;
            case PROJECT_MENU_NAME:
                interactionManager.projectMenuInteraction(entityStack);
                break;
            case PARTICIPANT_MENU_NAME:
                interactionManager.participantMenuInteraction();
                break;
            case ACCOUNT_MENU_NAME:
                interactionManager.accountMenuInteraction();
                break;
            case PROGRESS_MENU_NAME:
                try {
                    interactionManager.progressMenuInteraction();
                } catch (DateTimeParseException err) {
                    System.out.println("The date entered was invalid.");
                }
            case PERSON_MENU_NAME:
                interactionManager.personMenuInteraction();
                break;
        }
    }
}

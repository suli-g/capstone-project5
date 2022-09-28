/**
 * To get started follow these steps:
 * Open a terminal in the src/sql directory and enter the following:
 * > mysql -u <username> -p 
 * > (enter password here)
 * > source sql/setup.sql
 */

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
import Components.Menu.Menu;
import Controllers.EntityController;
import Controllers.InputController;
import Controllers.MenuController;
import Interfaces.IMenu;
import Models.DatabaseConnectionModel;
import Models.EntityModel.EntityModel;
import Utilities.OutputUtils;

/**
 * Contains this application's main execution loop.
 */
public class Main implements IMenu {
    private static EntityController entityController;
    private static MenuController menuController;
    private static Menu currentMenu;
    private static InputController interactionManager;

    /**
     * @param mainArgs the arguments supplied by the main function.
     * @throws SQLException if a database connection error occurs.
     */
    private static void initializeEntityManager(String[] mainArgs) {
        try {
            InputStream configFileStream;
            if (mainArgs.length == 0) {
                configFileStream = Main.class.getClassLoader().getResourceAsStream(DB_PROPERTIES_FILE);
            } else {
                configFileStream = new FileInputStream(mainArgs[0]);
            }
            entityController = EntityController.getInstance(
                    EntityModel.getInstance(DatabaseConnectionModel.loadFromFile(configFileStream)));
            menuController = MenuController.getInstance();
            menuController.addMenu(MAIN_MENU);
            interactionManager = InputController.getInstance(entityController, menuController);
        } catch (IOException ioError) {
            ioError.printStackTrace();
            System.out.println(DB_CONFIG_ERROR_MESSAGE);
            System.out.println(ioError);
            System.exit(1);
        } catch (SQLException sqlError) {
            sqlError.printStackTrace();
            System.out.println(DB_INIT_ERROR_MESSAGE);
            System.exit(1);
        } catch (NullPointerException configMissing) {
            configMissing.printStackTrace();
            // System.out.println(configMissing.getLocalizedMessage());
            System.exit(1);
        }
    }

    /**
     * @param args runtime arguments.
     */
    public static void main(String[] args) {
        Input.getInstance(new BufferedReader(new InputStreamReader(System.in)));
        initializeEntityManager(args);
        OutputUtils.printDoubleLine();
        OutputUtils.printHeading(COMPANY_NAME);
        OutputUtils.printLine();
        OutputUtils.printCentered(APPLICATION_TYPE);
        while (!menuController.isDone()) {
            try {
                currentMenu = menuController.showCurrent();
                if (currentMenu != null) {
                    handleInteraction();
                }
            } catch (InterruptedException aborted) {
                OutputUtils.printWarning(aborted.getLocalizedMessage());
            } catch (IllegalStateException error) {
                OutputUtils.printWarning(error.getLocalizedMessage());
            } catch (SQLException error) {
                error.printStackTrace();
                OutputUtils.printWarning(DB_ERROR_MESSAGE);
            } catch (IOException error) {
                OutputUtils.printWarning(INPUT_ERROR_MESSAGE);
            } catch (NumberFormatException error) {
                OutputUtils.printWarning(NUMBER_EXPECTED_MESSAGE);
            } catch (IllegalArgumentException error) {
                OutputUtils.printWarning(error.getLocalizedMessage());
            }
        }
        System.out.println(GOOD_BYE_MESSAGE);
    }

    /**
     * Handles all user interactions.
     * 
     * @throws SQLException if a database error occurs.
     * @throws IOException  if an I/O error occurs.
     */
    private static void handleInteraction() throws SQLException, IOException, InterruptedException {
        String command = currentMenu.getCommand();
        if (command == null) {

        }
        switch (command) {
            case MenuController.BACK_COMMAND:
                if (entityController.getTotalEntitiesLoaded() >= menuController.getTotalMenusLoaded()) {
                    entityController.popFromStack();
                }
                menuController.popTop();
                return;
            case MenuController.QUIT_COMMAND:
                menuController.clearStack();
                return;
        }

        switch (currentMenu.getName()) {
            case MAIN_MENU_NAME:
                interactionManager.mainMenuInteraction();
                break;
            case PROJECT_MENU_NAME:
                interactionManager.projectMenuInteraction();
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
                    System.out.println(INVALID_DATE_MESSAGE);
                }
                break;
            case PERSON_MENU_NAME:
                interactionManager.personMenuInteraction();
                break;
        }
    }
}

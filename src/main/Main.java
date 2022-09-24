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
import Components.Menu.Menu;
import Controller.EntityController;
import Controller.MenuController;
import Controller.ResponseController;
import Controller.ParticipantController;
import Interfaces.Constants;
import Interfaces.IMenu;
import Model.DatabaseConnection;
import Model.EntityModel.EntityModel;
import Utilities.OutputUtils;

/**
 * Contains this application's main execution loop.
 */
public class Main implements Constants, IMenu {
    private static EntityController entityController;
    private static MenuController menuController;
    private static ParticipantController participantController;
    private static Menu currentMenu;
    private static ResponseController interactionManager;

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
                    EntityModel.getInstance(DatabaseConnection.loadFromFile(configFileStream)));
            menuController = MenuController.getInstance();
            menuController.addMenu(MAIN_MENU);
            interactionManager = ResponseController.getInstance(entityController, menuController, participantController);
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
        } catch (NullPointerException configMissing) {
            OutputUtils.printWarning(configMissing.getLocalizedMessage());
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
            currentMenu = menuController.showCurrent();
            try {
                handleInteraction();
            } catch (SQLException error) {
                OutputUtils.printWarning("An error occurred while accessing the database.");
            } catch (IOException error) {
                OutputUtils.printWarning("An error occurred while while reading from input.");
            } catch (Menu.InvalidSelectionException error) {
                OutputUtils.printWarning("The value entered is not on the menu.");
            } 
            catch (NumberFormatException error) {
                OutputUtils.printWarning("Please enter a number.");
            }
            catch (IllegalStateException error) {
                OutputUtils.printWarning(error.getLocalizedMessage());
            }
            catch (IllegalArgumentException error) {
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
            case MenuController.BACK_COMMAND:
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
                    System.out.println("The date entered was invalid.");
                }
            case PERSON_MENU_NAME:
                interactionManager.personMenuInteraction();
                break;
        }
    }
}

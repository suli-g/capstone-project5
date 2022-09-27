package Factories;

import java.sql.ResultSet;
import java.sql.SQLException;

import Entities.Project;

/**
 * Represents the structure of a database row containing data about a project.
 */
interface ProjectRow {
    /**
     * The column name linked to the id of a project in the database
     */
    String PROJECT_ID = "project_id";
    /**
     * The column name linked to the name of a project in the database
     */
    String PROJECT_NAME = "project_name";
    /**
     * The column name linked to the address of a project in the database
     */
    String PROJECT_ADDRESS = "project_address";
    /**
     * The column name linked to the building type of a project in the database
     */
    String PROJECT_TYPE = "project_type";
    /**
     * The column name linked to the due date of a project in the database
     */
    String DATE_DUE = "date_due";
    /**
     * The column name linked to the date a project was finalized in the database
     */
    String DATE_FINALIZED = "date_finalized";
    /**
     * The column name linked to the erf number of a project's address in the database
     */
    String ERF_NUMBER = "erf_number";
    /**
     * The column name linked to the cost of a project in the database
     */
    String AMOUNT_DUE = "amount_due";
    /**
     * The column name linked to the amount paid for a project in the database
     */
    String AMOUNT_PAID = "amount_paid";
}

/**
 * Contains methods that allow easy creation of {@link Project} objects.
 */
public class ProjectFactory implements ProjectRow {
    /**
     * Creates a new {@link Project} object with the given parameters.
     * 
     * @param results the resultset containing the necessary columns.
     * @return a {@link Project} object with the given parameters.
     * @throws SQLException if {@code results} does not contain the required columns
     *                      database error occurs.
     */
    public static Project fromResults(ResultSet results) throws SQLException {
        return new Project(
                results.getInt(PROJECT_ID),
                results.getString(PROJECT_NAME),
                results.getString(PROJECT_ADDRESS),
                results.getString(PROJECT_TYPE))
                .setDueDate(results.getString(DATE_DUE))
                .setDateFinalized(results.getString(DATE_FINALIZED))
                .setErfNumber(results.getInt(ERF_NUMBER))
                .setCost((double) results.getInt(AMOUNT_DUE))
                .setPaid(results.getInt(AMOUNT_PAID));
    }
}

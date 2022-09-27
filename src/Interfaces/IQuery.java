package Interfaces;

/**
 * Contains strings used in this application for SQL queries.
 */
public interface IQuery extends Constants {
    /**
     * Used to verify that a string matches the phone_number column's
     * data type as recognized in the Database.
     */
    String DATE_FORMAT_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    /**
     * Used to verify that a string matches the phone_number column's
     * data type as recognized in the Database.
     */
    String PHONE_NUMBER_REGEX = "\\d{10}";
    /**
     * The message to be used if the phone number provided does not match
     * {@link #PHONE_NUMBER_REGEX}.
     */
    String PHONE_NUMBER_LIMIT_EXPLANATION = "The phone number should have exactly 10 numerical digits.";
    /**
     * The name of the view used to access incomplete projects.
     */
    String PROJECTS_VIEW = "projects";
    /**
     * The name of the view used to access incomplete projects.
     */
    String INCOMPLETE_PROJECTS_VIEW = "incomplete_projects";
    /**
     * The name of the view used to access complete projects.
     */
    String COMPLETE_PROJECTS_VIEW = "complete_projects";
    /**
     * The name of the view used to access finalized projects.
     */
    String FINALIZED_PROJECTS_VIEW = "finalized_projects";
    /**
     * The name of the view used to access outstanding projects.
     */
    String OUTSTANDING_PROJECTS_VIEW = "outstanding_projects";
    /**
     * The Message to be displayed when a record has been created.
     */
    String INSERT_SUCCESS_MESSAGE = "THE RECORD HAS SUCCESSFULLY BEEN CREATED.";
    /**
     * The Message to be displayed when a record has failed to be created.
     */
    String INSERT_FAILURE_MESSAGE = "Registration failed - no rows were affected.";
    /**
     * The Message to be displayed when a record has failed to be created.
     */
    String INSERT_FAILURE_MESSAGE_NO_ID = "Registration failed - no ID could be obtained.";
    /**
     * The Message to be displayed when a record has been updated.
     */
    String ADDRESS_INSERT_FAILURE_MESSAGE = "Address registration failed - no rows were affected.";
    /**
     * The Message to be displayed when a record has been updated.
     */
    String UPDATE_SUCCESS_MESSAGE = "THE RECORD HAS SUCCESSFULLY BEEN UPDATED.";
    /**
     * The Message to be displayed when a record has failed to be updated.
     */
    String UPDATE_FAILURE_MESSAGE = "THE RECORD COULD NOT BE UPDATED.";
    /**
     * The message to show when no types have been defined in the database.
     */
    String NO_TYPES = "No relationship or building types have been defined.";
}
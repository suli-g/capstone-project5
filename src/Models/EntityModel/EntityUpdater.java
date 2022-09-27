package Models.EntityModel;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import Components.QueryBuilder;

public abstract class EntityUpdater extends EntityQuerier {
    public EntityUpdater(QueryBuilder queryBuilder) {
        super(queryBuilder);
    }

    /**
     * Insert the details of a person into the database.
     * 
     * @param firstName    of the person.
     * @param lastName     of the person.
     * @param emailAddress of the person.
     * @param phoneNumber  of the person.
     * @param erfNumber    of the person's address.
     * @return {@value 1} if the update is successful, {@value 0} if not successful.
     * @throws SQLException if the registration fails.
     */
    public PreparedStatement registerPerson(String firstName, String lastName, String emailAddress, String phoneNumber, int erfNumber)
            throws SQLException {
        PreparedStatement statement = queryBuilder.insertInto("person")
                .group("first_name", "last_name", "phone_number", "email_address", "physical_address")
                .values(5).prepare("person_id");
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, phoneNumber);
        statement.setString(4, emailAddress);
        statement.setInt(5, erfNumber);
        return statement;
    }

    /**
     * @param projectId of the project.
     * @param personId  of the person.
     * @param role      for which the person should be registered.
     * @return {@value 1} if the update is successful, {@value 0} if not successful.
     * @throws SQLException if the registration fails.
     */
    public PreparedStatement registerParticipant(int projectId, int personId, String role) throws SQLException {
        PreparedStatement statement = queryBuilder.insertInto("participant")
                .group("project_id", "person_id", "relationship")
                .values(3).prepare("participant_id");
        statement.setInt(1, projectId);
        statement.setInt(2, personId);
        statement.setString(3, role);
        return statement;
    }

    /**
     * @param projectName the name of the project.
     * @param projectType the building type of the project.
     * @param erfNumber   of the project's address.
     * @return {@value 1} if the update is successful, {@value 0} if not successful.
     * @throws SQLException if the registration fails.
     */
    public PreparedStatement registerProject(String projectName, String projectType, int erfNumber)
            throws SQLException {
        String[] columns = new String[] { "project_type", "project_address", null };
        int values = 2;
        if (projectName != null) {
            columns[values++] = "project_name";
        }

        PreparedStatement statement = queryBuilder
                .insertInto("project")
                .group(columns).values(values).prepare("project_id");

        statement.setString(1, projectType);
        statement.setInt(2, erfNumber);
        if (columns[values - 1] != null) {
            statement.setString(values, projectName);
        }
        return statement;
    }

    /**
     * @param erfNumber     of the address.
     * @param streetAddress of the address.
     * @param suburb        of the address.
     * @param city          of the address.
     * @param province      of the address.
     * @param postCode      of the address.
     * @return {@value 1} if the update is successful, {@value 0} if not successful.
     * @throws SQLException if the registration fails.
     */
    public PreparedStatement registerAddress(int erfNumber, String streetAddress, String suburb, String city, String province,
            int postCode) throws SQLException {
        PreparedStatement statement = queryBuilder.insertInto("address")
                .group("erf_number", "street_address", "suburb", "city",
                        "province", "post_code")
                .values(6).prepare("address_id");
        statement.setInt(1, erfNumber);
        statement.setString(2, streetAddress);
        statement.setString(3, suburb);
        statement.setString(4, city);
        statement.setString(5, province);
        statement.setString(6, String.valueOf(postCode));
        return statement;
    }

    /**
     * Updates the progress of a project in the database.
     * 
     * @param projectId     the id of the project to update.
     * @param dueDate       the new due date of the project.
     * @param dateFinalized the new date finalized of the project.
     * @return {@value 1} if the update is successful, {@value 0} if not successful.
     * @throws SQLException if the update fails.
     */
    public boolean updateProgress(int projectId, String dueDate, String dateFinalized) throws SQLException {
        QueryBuilder query = queryBuilder.update("progress");
        boolean dateDueIsDate = dueDate.matches(DATE_FORMAT_REGEX),
                dateFinalizedIsDate = dateFinalized.matches(DATE_FORMAT_REGEX);
        int queryIndex = 1;
        if (dateDueIsDate && dateFinalizedIsDate) {
            query.set("date_due", "date_finalized");
        } else if (dateFinalizedIsDate) {
            query.set("date_finalized");
        } else {
            query.set("date_due");
        }
        PreparedStatement update = query.where("project").prepare();
        if (dateDueIsDate && dateFinalizedIsDate) {
            update.setString(queryIndex++, dueDate);
            update.setString(queryIndex++, dateFinalized);
        } else if (dateFinalizedIsDate) {
            update.setString(queryIndex++, dateFinalized);
        } else {
            update.setString(queryIndex++, dueDate);
        }
        update.setInt(queryIndex, projectId);
        return update.executeUpdate() == 1;
    }

    /**
     * Updates the account details of the project.
     * 
     * @param projectId  of the project.
     * @param amountDue  the new cost of the project.
     * @param amountPaid the new amount paid for the project.
     * @return {@value 1} if the update is successful, {@value 0} if not successful.
     * @throws SQLException if the update fails.
     */
    public boolean updateAccount(int projectId, int amountDue, int amountPaid) throws SQLException {
        PreparedStatement update = queryBuilder.update("account")
                .set("amount_due", "amount_paid")
                .where("project").prepare();
        update.setInt(1, amountDue);
        update.setInt(2, amountPaid);
        update.setInt(3, projectId);
        return update.executeUpdate() == 1;
    }

    /**
     * Updates the contact details of a person.
     * 
     * @param personId     of the person.
     * @param phoneNumber  the new phone number of the person.
     * @param emailAddress the new email address of the person.
     * @return {@value 1} if the update is successful, {@value 0} if not successful.
     * @throws SQLException if the update fails.
     */
    public boolean updateContactDetails(int personId, String phoneNumber, String emailAddress) throws SQLException {
        boolean updatePhone = false;
        QueryBuilder query = queryBuilder.update("person");
        int updates;
        if (phoneNumber != null && emailAddress != null) {
            query.set("phone_number", "email_address");
            updatePhone = true;
            updates = 2;
        } else if (phoneNumber != null) {
            query.set("phone_number");
            updatePhone = true;
            updates = 1;
        } else if (emailAddress != null) {
            query.set("email_address");
            updates = 1;
        } else {
            return false;
        }
        PreparedStatement statement = query.where("person_id").prepare();
        if (updates == 1) {
            if (updatePhone) {
                statement.setString(1, phoneNumber);
            } else {
                statement.setString(1, emailAddress);
            }
        } else {
            statement.setString(1, phoneNumber);
            statement.setString(2, emailAddress);
        }
        statement.setInt(updates + 1, personId);
        return statement.executeUpdate() == 1;
    }
}

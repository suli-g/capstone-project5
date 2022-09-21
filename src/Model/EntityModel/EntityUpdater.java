package Model.EntityModel;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import Components.QueryBuilder;

public abstract class EntityUpdater extends EntityQuerier {
    public EntityUpdater(QueryBuilder qb) {
        super(qb);
    }

    public int updateProgress(int projectId, String[] columns, String[] dates) throws SQLException {
        if (dates.length != columns.length) {
            throw new IllegalArgumentException("Each column entry should have an associated 'date' entry.");
        }
        QueryBuilder query = queryBuilder.update("progress");
        int unknowns = 0;
        query.set(columns).where("project_id");
        PreparedStatement statement = query.prepare();
        for (int i = 0; i < dates.length; i++) {
            statement.setString(i + 1, dates[i]);
        }
        statement.setInt(unknowns + 1, projectId);
        return statement.executeUpdate();
    }

    public int registerPerson(String firstName, String lastName, String emailAddress, String phoneNumber, int erfNumber)
            throws SQLException {
        PreparedStatement statement = queryBuilder.insert("person")
        .columns("first_name", "last_name", "phone_number", "email_address", "physical_address")
        .values(5).prepare();
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, phoneNumber);
        statement.setString(4, emailAddress);
        statement.setInt(5, erfNumber);
        return statement.executeUpdate();
    }

    public int registerParticipant(int projectId, int personId, String role) throws SQLException {
        PreparedStatement statement = queryBuilder.insert("participant")
                .columns("project_id", "person_id", "relationship")
                .values(3).prepare();
        statement.setInt(1, projectId);
        statement.setInt(2, personId);
        statement.setString(3, role);
        return statement.executeUpdate();
    }

    public int registerProject(String projectName, String projectType, int erfNumber)
            throws SQLException {
        String[] columns = new String[]{"project_type","project_address",null};
                int values = 2;
        if (projectName != null) {
            columns[values++] = "project_name";
        }
        
        PreparedStatement statement = queryBuilder
                .insert("project")
                .columns(columns).values(values).prepare();

        statement.setString(1, projectType);
        statement.setInt(2, erfNumber);
        if (columns[values - 1] != null) {
            statement.setString(values, projectName);
        }
        return statement.executeUpdate();
    }

    public int registerAddress(int erfNumber, String streetAddress, String suburb, String city, String province,
            int postCode) throws SQLException {
        PreparedStatement statement = queryBuilder.insert("address")
            .columns("erf_number", "street_address", "suburb", "city",
                "province", "post_code").values(6).prepare();
        statement.setInt(1, erfNumber);
        statement.setString(2, streetAddress);
        statement.setString(3, suburb);
        statement.setString(4, city);
        statement.setString(5, province);
        statement.setString(6, String.valueOf(postCode));
        int result = statement.executeUpdate();
        return result;
    }

    public int updateProgress(int projectId, String dueDate, String dateFinalized) throws SQLException {
        QueryBuilder query = queryBuilder.update("progress");
        boolean dateDueIsDate = dueDate.matches(DATE_FORMAT_REGEX),
                dateFinalizedIsDate = dateFinalized.matches(DATE_FORMAT_REGEX);
        int queryIndex = 1;
        if (dateDueIsDate && dateFinalizedIsDate) {
            query.set("date_due", "date_finalized");
        }
        else if (dateFinalizedIsDate) {
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
        return update.executeUpdate();
    }

    public int updateAccount(int projectId, int amountDue, int amountPaid) throws SQLException {
        PreparedStatement update = queryBuilder.update("account")
                .set("amount_due", "amount_paid")
                .where("project").prepare();
        update.setInt(1, amountDue);
        update.setInt(2, amountPaid);
        update.setInt(3, projectId);
        return update.executeUpdate();
    }

    public int updateContactDetails(int personId, String phoneNumber, String emailAddress) throws SQLException {
        PreparedStatement update = queryBuilder.update("person")
                .set("phone_number", "email_address")
                .where("person_id").prepare();
        update.setString(1, phoneNumber);
        update.setString(2, emailAddress);
        update.setInt(3, personId);
        return update.executeUpdate();
    }
}

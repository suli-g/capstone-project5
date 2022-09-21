package Model.EntityModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Components.QueryBuilder;

public abstract class EntityQuerier extends QueryModel {
    public EntityQuerier(QueryBuilder queryBuilder) {
        super(queryBuilder);
    }

    public ResultSet getProjects(PROJECT_VIEW status) throws SQLException {
        String table = status.LABEL;
        PreparedStatement statement = queryBuilder.select(table, "*").prepare();
        ResultSet results = statement.executeQuery();
        if (!results.next()) {
            return null;
        }
        return results;
    }

    public ResultSet verifyPerson(String phoneNumber, int erfNumber) throws SQLException {
        PreparedStatement query = queryBuilder
                .select("person",
                        "first_name",
                        "last_name",
                        "email_address",
                        "phone_number",
                        "erf_number")
                .join(QueryBuilder.JOIN_TYPE.RIGHT_JOIN, "address", "erf_number", "physical_address")
                .where("phone_number")
                .or("erf_number").prepare();
        query.setString(1, phoneNumber);
        query.setInt(2, erfNumber);
        ResultSet results = query.executeQuery();
        if (results.next()) {
            return results;
        }
        return null;
    }

    public ResultSet selectProject(int projectId) throws SQLException {
        PreparedStatement query = queryBuilder
                .select("projects", "*")
                .where("project_id")
                .prepare();
        query.setInt(1, projectId);
        ResultSet results = query.executeQuery();
        if (results.next()) {
            return results;
        }
        return null;
    }

    public ResultSet getParticipants(int projectNumber) throws SQLException {
        PreparedStatement selection = queryBuilder
                .select("participants", "*")
                .where("project_id").prepare();
        selection.setInt(1, projectNumber);
        ResultSet results = selection.executeQuery();
        if (results.next()) {
            return results;
        }
        return null;
    }

    public ResultSet loadTypes() throws SQLException {
        ResultSet results = queryBuilder.select("types_view", "relationship_type", "building_type")
                .prepare().executeQuery();

        if (results.next()) {
            return results;
        }
        return null;
    }

    public ResultSet selectPerson(String phoneNumber) throws SQLException {
        PreparedStatement query = queryBuilder.select("people",
                "person_id",
                "first_name",
                "last_name",
                "email_address",
                "phone_number",
                "physical_address").where("phone_number")
                .prepare();
        query.setString(1, phoneNumber);
        ResultSet results = query.executeQuery();
        if (results.next()) {
            return results;
        }
        return null;
    }

    public ResultSet selectPerson(int personId) throws SQLException {
        PreparedStatement query = queryBuilder.select("people",
                "person_id",
                "first_name",
                "last_name",
                "email_address",
                "phone_number",
                "physical_address").where("person_id")
                .prepare();
        query.setInt(1, personId);
        ResultSet results = query.executeQuery();
        if (results.next()) {
            return results;
        }
        return null;
    }

    public ResultSet getAddress(int erfNumber) throws SQLException {
        PreparedStatement query = queryBuilder.select("addresses", "full_address")
                .where("erf_number").prepare();
        query.setInt(1, erfNumber);
        ResultSet results = query.executeQuery();
        if (results.next()) {
            return results;
        }
        return null;
    }
}

package Model.EntityModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Components.QueryBuilder;

public abstract class EntityQuerier extends QueryModel {
    public EntityQuerier(QueryBuilder queryBuilder) {
        super(queryBuilder);
    }

    /** 
     * @param view
     * @return ResultSet
     * @throws SQLException
     */
    public ResultSet getProjects(String view) throws SQLException {
        PreparedStatement statement = queryBuilder.select(view, "*").prepare();
        ResultSet results = statement.executeQuery();
        if (!results.next()) {
            return null;
        }
        return results;
    }

    /** 
     * @param projectId
     * @return ResultSet
     * @throws SQLException
     */
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

    
    /** 
     * @param projectNumber
     * @return ResultSet
     * @throws SQLException
     */
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

    
    /** 
     * @return ResultSet
     * @throws SQLException
     */
    public ResultSet loadTypes() throws SQLException {
        ResultSet results = queryBuilder.select("types_view", "relationship_type", "building_type")
                .prepare().executeQuery();

        if (results.next()) {
            return results;
        }
        return null;
    }

    
    /** 
     * @param phoneNumber
     * @return ResultSet
     * @throws SQLException
     */
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

    
    /** 
     * @param personId
     * @return ResultSet
     * @throws SQLException
     */
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

    
    /** 
     * @param erfNumber
     * @return ResultSet
     * @throws SQLException
     */
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

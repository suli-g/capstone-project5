package Components;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Entities.Project;
import config.Overviews;
import config.Queries;

public class ProjectModel implements Queries, Overviews {
    private Database database;
    private QueryBuilder queryBuilder;
    private static ProjectModel modelInstance;

    public static ProjectModel getInstance(Database db) {
        if (modelInstance == null) {
            modelInstance = new ProjectModel(db);
        }
        return modelInstance;
    }

    private ProjectModel(Database database) {
        this.database = database;
        this.queryBuilder = QueryBuilder.getInstance(database);
    }

    public void setRequiredRoles() throws SQLException {
        ResultSet results = queryBuilder.select("*", "relationship")
                .prepare().executeQuery();
        ArrayList<String> roles = new ArrayList<>();
        while (results.next()) {
            String role = results.getString("relationship_type");
            roles.add(role);
        }
        Project.setRequiredRoles(roles);
    }

    public ArrayList<Project> getProjectList(PROJECT_STATUS status) throws SQLException, ProjectNotFoundException {
        String table;
        switch (status) {
            case ANY:
                table = "projects";
                break;
            case INCOMPLETE:
                table = "incomplete_projects";
                break;
            case OUTSTANDING:
                table = "outstanding_projects";
                break;
            case FINALIZED:
                table = "finalized_projects";
                break;
            default:
                throw new IllegalArgumentException("Invalid argument provided.");
        }
        PreparedStatement statement = queryBuilder.select("*", table).prepare();
        ResultSet results = statement.executeQuery();
        if (!results.next()) {
            throw new ProjectNotFoundException();
        }
        ArrayList<Project> projects = new ArrayList<>();
        do {
            Project currentProject = new Project(
                results.getInt("project_id"),
                results.getString("project_name"),
                results.getString("project_address"),
                results.getString("project_type")
            )
            .setErfNumber(results.getInt("erf_number"))
            .setCost(results.getInt("amount_due"))
            .setPaid(results.getInt("amount_paid"))
            .setDueDate(results.getString("date_due"));
            String dateFinalized = results.getString("date_finalized");
            if (dateFinalized == null) {
                dateFinalized = "n/a";
            } else {
                currentProject.markFinalized(dateFinalized);
            }
            projects.add(
                currentProject
            );
        } while (results.next());
        for (Project p: projects) {
            System.out.println(p);
        }
        return projects;
    }

    public Project selectProject(int projectId) throws SQLException, ProjectNotFoundException {
        PreparedStatement query = queryBuilder
                .select("*", "projects")
                .where("project_id", "?")
                .prepare();
        query.setInt(1, projectId);
        ResultSet results = query.executeQuery();
        if (!results.next()) {
            throw new ProjectNotFoundException();
        }
        return new Project(
                results.getInt("project_id"),
                results.getString("project_name"),
                results.getString("project_address"),
                results.getString("project_type"))
                .setErfNumber(results.getInt("erf_number"))
                .setCost((double) results.getInt("amount_due"))
                .setPaid(results.getInt("amount_paid"));
    }

    public boolean listParticipants(int projectId) throws SQLException, ProjectNotFoundException {
        QueryBuilder query = queryBuilder.select("*", "roles")
                .where("project_id", "?");
        for (String role : Project.getRequiredRoles()) {
            query.select(PARTICPANT_QUERY_DETAILS, role);
            query.join(QueryBuilder.JOIN_TYPE.CROSS_JOIN, "contacts", role, "phone_number", role);
        }
        query.where("project", projectId);
        System.out.println(query.toString());
        return true;
    }

    public class ProjectNotFoundException extends Exception {
        public ProjectNotFoundException() {
            super(QUERY_GETS_NO_RESULTS);
        }
    }
}

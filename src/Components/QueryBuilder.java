package Components;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import Config.Queries;
import Model.DatabaseModel;

public class QueryBuilder implements Queries {
    private String lastQueryString;
    public String getLastQueryString() {
        return lastQueryString;
    }

    public String setLastQueryString(String lastQueryString) {
        query = new StringBuilder();
        this.lastQueryString = lastQueryString;
        return lastQueryString;
    }

    private StringBuilder query;
    private StringBuilder getQuery() {
        if (query == null) {
            query = new StringBuilder();
        }
        return query;
    }

    private static QueryBuilder builderInstance;

    public static QueryBuilder getInstance(DatabaseModel dbInstance) {
        if (builderInstance == null) {
            builderInstance = new QueryBuilder(dbInstance);
        }
        return builderInstance;
    }

    private DatabaseModel dbInstance;

    private QueryBuilder(DatabaseModel dbInstance) {
        this.dbInstance = dbInstance;
    }

    public QueryBuilder columns(String...columnNames) {
        assert query != null;
        query.append("(").append(
            String.join(
                ",", Arrays.stream(columnNames)
                        .filter(name -> name != null).toList()))
        .append(") ");
        return this;
    }


    public QueryBuilder values(String... values) {
        assert query != null;
        query.append(" VALUES (").append(String.join(",", values)).append(")");
        return this;
    }

    public QueryBuilder values(int values) {
        assert query != null;
        String[] placeholders = new String[values];
        Arrays.fill(placeholders, "?");
        query.append(" VALUES (").append(String.join(",", placeholders)).append(")");
        return this;
    }

    public QueryBuilder update(String[] tables) {
        return update(String.join(",", tables));
    }

    public QueryBuilder insert(String intoTable) throws SQLException {
        getQuery();
        query.append("INSERT INTO ").append(intoTable);
        return this;
    }

    public QueryBuilder update(String tableName) {
        getQuery();
        query.append("UPDATE ").append(tableName);
        return this;
    }

    public QueryBuilder set(String... columnNames) {
        getQuery();
        int columnAmount = columnNames.length;
        query.append(" SET ");
        for (int i = 0; i < columnAmount; i++) {
            query.append(columnNames[i]).append("=?");
            if (i < columnAmount - 1 ) {
                query.append(",");
            }
        }
        return this;
    }

    public QueryBuilder join(JOIN_TYPE joinType, String tableName, String onColumnName, String onColumnValue) {
        getQuery();
        query.append(" ").append(joinType.getValue())
            .append(tableName)
                .append(" ON ")
                .append(onColumnName)
                .append("=")
                .append(onColumnValue);
        return this;
    }

    public QueryBuilder select(String fromTable, String... columnNames) throws SQLException {
        getQuery();
        query.append("SELECT ").append(String.join(",", columnNames)).append(" FROM ").append(fromTable);
        return this;
    }

    public QueryBuilder where(String columnName) {
        getQuery();
        query.append(" WHERE ").append(columnName).append("=?");
        return this;
    }

    public QueryBuilder and(String columnName) throws SQLException{
        getQuery();
        query.append(" AND ").append(columnName).append("=?");
        return this;
    }

    public QueryBuilder or(String columnName) {
        getQuery();
        query.append(" OR ").append(columnName).append("=?");
        return this;
    }

    public PreparedStatement prepare() throws SQLException {
        String statement = query.toString();
        query = null;
        return dbInstance.prepare(statement);
    }

    @Override
    public String toString() {
        setLastQueryString(query.toString());
        return query.toString();
    }
}
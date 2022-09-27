package Components;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import Interfaces.IQuery;
import Models.DatabaseConnectionModel;

/**
 * Represents a QueryBuilder.
 */
public class QueryBuilder implements IQuery { 
    private StringBuilder query;
    
    /** 
     * Makes sure that {@link #query} is initialized as a {@link StringBuilder}
     * 
     * @return the {@code query} {@link StringBuilder}
     */
    private StringBuilder getQuery() {
        if (query == null) {
            query = new StringBuilder();
        } else {
            query.append(" ");
        }
        return query;
    }

    /** 
     * Appends {@code items} - as a comma-separated list within parenthesis - to {@link #query}.
     * 
     * @param items the items to be grouped
     * @return this QueryBuilder instance
     */
    public QueryBuilder group(String ...items) {
        getQuery();
        query.append("(").append(
            String.join(
                ",", Arrays.stream(items)
                        .filter(name -> name != null).toList()))
        .append(") ");
        return this;
    }

    /** 
     * Appends a SQL "VALUES" clause to {@link #query}.
     * 
     * @param values the comma-separated list of column names to be included in the "VALUES" clause.
     * @return this QueryBuilder instance
     */
    public QueryBuilder values(String... values) {
        getQuery();
        query.append("VALUES");
        group(values);
        return this;
    }

    /** 
     * Calls {@link #values(String...)} with a an array of size {@code placeholderAmount} of placeholders for the resulting {@link PreparedStatement}, which this class uses.
     * 
     * @param placeholderAmount the amount of placeholders to produce
     * @return this QueryBuilder instance
     */
    public QueryBuilder values(int placeholderAmount) {
        getQuery();
        String[] placeholders = new String[placeholderAmount];
        Arrays.fill(placeholders, "?");
        values(placeholders);
        return this;
    }

    /** 
     * Appends a SQL "INSERT INTO" clause to {@link #query} followed by {@code tableName}.
     * 
     * @param tableName the table name to append
     * @return this QueryBuilder instance
     */
    public QueryBuilder insertInto(String tableName) {
        getQuery();
        query.append("INSERT INTO ").append(tableName);
        return this;
    }

    /** 
     * Appends "UPDATE {@code tableName}" to {@link #query}.
     * 
     * @param tableName the table to be updated
     * @return this QueryBuilder instance
     */
    public QueryBuilder update(String tableName) {
        getQuery();
        query.append("UPDATE ").append(tableName);
        return this;
    }

    /** 
     * Appends "SET " and "{@code columnName=?}" for each {@code columnName} in {@code columnNames} to {@link #query}.
     * 
     * @param columnNames the columns to equate to placeholders
     * @return this QueryBuilder instance
     */
    public QueryBuilder set(String... columnNames) {
        getQuery();
        int columnAmount = columnNames.length;
        query.append("SET ");
        for (int i = 0; i < columnAmount; i++) {
            query.append(columnNames[i]).append("=?");
            if (i < columnAmount - 1 ) {
                query.append(",");
            }
        }
        return this;
    }

    /** 
     * Appends "SELECT ", followed by a comma-separated list of {@code columnName} for each columnName in {@code columnNames}, and
     * "FROM {@param tableName}" to {@link #query}.
     *  
     * @param tableName the table name to select from
     * @param columnNames the columns to select
     * @return this QueryBuilder instance
     */
    public QueryBuilder select(String tableName, String... columnNames) {
        getQuery();
        query.append("SELECT ").append(String.join(",", columnNames)).append(" FROM ").append(tableName);
        return this;
    }

    /** 
     * Appends "WHERE {@code columnName}=?" to {@link #query}.
     * 
     * @param columnName the left hand side of the {@code =}
     * @return this QueryBuilder instance
     */
    public QueryBuilder where(String columnName) {
        getQuery();
        query.append(" WHERE ").append(columnName).append("=?");
        return this;
    }

    /** 
     * Appends "AND {@code columnName}=?" to {@link #query}.
     * 
     * @param columnName the left hand side of the {@code =}
     * @return this QueryBuilder instance
     */
    public QueryBuilder and(String columnName) {
        getQuery();
        query.append(" AND ").append(columnName).append("=?");
        return this;
    }

    /** 
     * Appends "OR {@code columnName}=?" to {@link #query}.
     * 
     * @param columnName the left hand side of the {@code =}
     * @return this QueryBuilder instance
     */
    public QueryBuilder or(String columnName) {
        getQuery();
        query.append(" OR ").append(columnName).append("=?");
        return this;
    }
    
    /** 
     * Creates a {@link PreparedStatement} from {@link #query} as a {@link String} and reinitializes {@code query}.
     * 
     * @return the created {@link PreparedStatement}
     * @throws SQLException if a database access error occurs.
     */
    public PreparedStatement prepare() throws SQLException {
        String statement = query.toString();
        query = null;
        return dbInstance.prepare(statement);
    }
    
    /** 
     * Creates a {@link PreparedStatement} from {@link #query} as a {@link String} and reinitializes {@code query}.
     * 
     * @see <a href="https://stackoverflow.com/a/1915197/2850190">answer by rogerdpack</a>
     * @param generatedKeys the keys generated by an insert or update operation.
     * @return the created {@link PreparedStatement}
     * @throws SQLException if a database access error occurs.
     */
    public PreparedStatement prepare(String... generatedKeys) throws SQLException {
        String statement = query.toString();
        query = null;
        return dbInstance.prepare(statement, generatedKeys);
    }

    private DatabaseConnectionModel dbInstance;
    private static QueryBuilder builderInstance;
    /** 
     * Creates a new QueryBuilder instance for this java application if no instance exists, or returns
     * the current instance.
     * 
     * @param dbInstance the database model to use to prepare statements
     * @return this application's QueryBuilder instance
     */
    public static QueryBuilder getInstance(DatabaseConnectionModel dbInstance) {
        if (builderInstance == null) {
            builderInstance = new QueryBuilder(dbInstance);
        }
        return builderInstance;
    }

    /** 
     * @return a string representation of {@link #query}.
     */
    @Override
    public String toString() {
        return query.toString();
    }

    /**
     * The constructor for this QueryBuilder instance.
     * @param dbInstance
     */
    private QueryBuilder(DatabaseConnectionModel dbInstance) {
        this.dbInstance = dbInstance;
    }
}
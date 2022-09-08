package Components;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class QueryBuilder {
    public static enum JOIN_TYPE {
        CROSS_JOIN("CROSS"),
        LEFT_JOIN("LEFT"),
        RIGHT_JOIN("RIGHT");
        private String value;
        private JOIN_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return String.format("%s JOIN ON ", value);
        }
    }
    private StringBuilder query,
            selection,
            joins,
            where;
    int pivotIndex;
    private static QueryBuilder builderInstance;
    public static QueryBuilder getInstance(Database dbInstance) {
        if (builderInstance == null) {
            builderInstance = new QueryBuilder(dbInstance);
        }
        return builderInstance;
    }

    private Database dbInstance;
    private QueryBuilder(Database dbInstance) {
        query = new StringBuilder();
        this.dbInstance = dbInstance;
    }

    public QueryBuilder join(JOIN_TYPE joinType, String table, String leftColumn, String rightColumn) {
        if (joins == null) {
            joins = new StringBuilder();
        }
        joins.append(joinType).append(table).append(" ON ").append(table).append(".").append(leftColumn).append("=").append(rightColumn);
        return this;
    }

    public QueryBuilder join(JOIN_TYPE joinType, String table, String alias, String leftColumn, String rightColumn) {
        return join(joinType, String.format("%s AS %s", table, alias), leftColumn, rightColumn);
    }

    public QueryBuilder select(String[] columns, String table) {
        if (selection == null) {
            selection = new StringBuilder();
        }
        selection.append(
            Arrays.asList(columns).stream().map(col -> String.format("%1$s AS %2$s.%1$s,", col, table))
        );
        selection.append(" FROM ").append(table);
        return this;
    }

    public QueryBuilder select(String column, String table) {
        if (selection == null) {
            selection = new StringBuilder();
        }
        selection.append(column).append(" FROM ").append(table);
        return this;
    }

    public QueryBuilder where(String columnName, Object value) {
        if (where == null) {
            where = new StringBuilder();
        }
        if (where.length() > 0) {
            where.append(" AND ");
        } else {
            where.append(" WHERE ");
        }
        where.append(String.format("%s = %s", columnName, value));
        return this;
    }

    public PreparedStatement prepare() throws SQLException {
        if (selection != null) {
            query.append("SELECT ").append(selection);
        }
        if (joins != null) {
            query.append(" ").append(joins);
        }
        if (where != null) {
            query.append(where);
        }

        String queryString = query.toString();
        if (query != null) {
            query.setLength(0);
        } if (joins != null) {
            joins.setLength(0);
        }
        if (where != null) {
            where.setLength(0);
        }
        if (selection != null) {
            selection.setLength(0);
        }
        return dbInstance.prepare(queryString);
    }

    @Override
    public String toString() {
        return query.toString();
    }
}
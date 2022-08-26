package IO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Represents a DataSource that reads and writes to a single file.
 */
public class Database {
    private Statement conn;
    private static final String URL_TEMPLATE = "jdbc:mysql://%s:%d/%s";
    private static Database instance;

    private static String getUrlFromProperties(String url, int port, String name) {
        return String.format(URL_TEMPLATE, url, port, name);
    }

    public Database(Connection connection) throws SQLException {
        this.conn = connection.createStatement();
    }

    public static Database load(Properties dbConfig) throws NumberFormatException, SQLException{
        if (instance == null) {
            String portString = dbConfig.getProperty("db.port"),
                name = dbConfig.getProperty("db.name"),
                user = dbConfig.getProperty("db.user");
            int port = Integer.parseInt(portString);
            String url = getUrlFromProperties(dbConfig.getProperty("db.url"), port,  name);
            instance = new Database(
                DriverManager.getConnection(url, user, dbConfig.getProperty("db.password"))
            );
        }
        return instance;
    }

    /**
     * Reads a line from the file at {@code sourcePath}.
     * 
     * @return the line that was read as a {@link String}.
     */
    public ResultSet query(String query) throws SQLException {
        return conn.executeQuery(query);
    }

    /**
     * Writes text to the file at {@code sourcePath}.
     * 
     * @param text the data to be written.
     * @return true if writing was successful, false otherwise.
     */
    public int update(String query) throws SQLException {
        return conn.executeUpdate(query);
    }
}
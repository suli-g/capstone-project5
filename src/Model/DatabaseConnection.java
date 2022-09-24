package Model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

interface DatabaseConnectionTemplates {
    /**
     * The template used for the url to establish the Database connection.
     */
    String URL_TEMPLATE = "jdbc:mysql://%s:%d/%s?useSSL=%s";
    /**
     * The key containing the database's url.
     */
    String DB_URL = "db.url";
    /**
     * The key containing the database's port.
     */
    String DB_PORT = "db.port";
    /**
     * The key containing the database's name.
     */
    String DB_NAME = "db.name";
    /**
     * The key containing the database's username.
     */
    String DB_USER = "db.user";
    /**
     * The key containing the database's password.
     */
    String DB_PASSWORD = "db.password";
    /**
     * The key indicating whether the useSSL option should be true (Default:
     * false}).
     */
    String DB_USE_SSL = "db.use-ssl";
}

/**
 * Represents a connection to the application database.
 */
public class DatabaseConnection implements DatabaseConnectionTemplates {
    /**
     * The database connection object for this class.
     */
    private Connection connection;
    /**
     * The single instance of a DatabaseConnection object.
     */
    private static DatabaseConnection instance;

    /**
     * The constructor for DatabaseConnection.
     * 
     * @param connection the established connection.
     */
    private DatabaseConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Reads the text in {@code configFileStream} into a {@link Properties} object
     * and passes the result to {@link #getConnection(Properties)}.
     * 
     * @see #getConnection(Properties)
     * 
     * @param configFileStream stream to a textfile containing the required
     *                         properties.
     * @return The result of {@link #getConnection(Properties)}.
     * @throws IOException  If an error occured while reading from
     *                      {@code configFileStream}.
     * @throws SQLException If the database connection fails
     */
    public static DatabaseConnection loadFromFile(InputStream configFileStream) throws IOException, SQLException {
        Properties config = new Properties();
        if (configFileStream == null) {
            throw new IOException("The configuration file could not be accessed");
        }
        config.load(configFileStream);
        return getConnection(config);
    }

    /**
     * <p>
     * Uses the data in {@code dbConfig}(@see {@link #DatabaseConnection}) to
     * establish a single connection with the
     * {@link DriverManager}.
     * 
     * This method will not establish a new connection even if the properties are
     * changed.
     * 
     * @param dbConfig the configuration to use for the connection
     * @return The instance of {@link #DatabaseConnection(Connection)} for this java
     *         application.
     * @throws NumberFormatException If the {@code db.port} value is not a number
     * @throws SQLException          If the database connection fails
     */
    public static DatabaseConnection getConnection(Properties dbConfig)
            throws NullPointerException, NumberFormatException, SQLException {
        if (instance == null) {
            String name = dbConfig.getProperty(DB_NAME),
                    user = dbConfig.getProperty(DB_USER),
                    url = dbConfig.getProperty(DB_URL),
                    portString = dbConfig.getProperty(DB_PORT);
            if (name == null || user == null || portString == null || url == null) {
                throw new NullPointerException(
                        "The database configuration file does not contain all of the required keys.");
            }
            int port = Integer.parseInt(portString);
            String connectionUrl = String.format(URL_TEMPLATE, url, port, name, dbConfig.getProperty(DB_USE_SSL, "false"));
            instance = new DatabaseConnection(
                    DriverManager.getConnection(connectionUrl, user, dbConfig.getProperty(DB_PASSWORD, "")));
        }
        return instance;
    }

    /**
     * Creates a {@link PreparedStatement} from the given {@code query}.
     * 
     * @param query a valid SQL statement.
     * @return the created {@link PreparedStatement}.
     * @throws SQLException if a database access error occurs.
     */
    public PreparedStatement prepare(String query) throws SQLException {
        return connection.prepareStatement(query);
    }
}
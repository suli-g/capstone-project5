package Model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Represents a DataSource that reads and writes to a single file.
 * 
 *  
 * @param db.name The database name.
 * @param db.url The database url.
 * @param db.port The database port.
 * @param db.user The database username.
 * @param db.password The database password.
 */
public class DatabaseConnection {
    /**
     * The database connection object for this class.
     */
    private Connection connection;
    /**
     * The template used for the database connection url.
     */
    private static final String URL_TEMPLATE = "jdbc:mysql://%s:%d/%s";
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
     * Reads the properties used for {@link #getConnection(Properties)} from {@code configFileStream}.
     * 
     * @return The result of {@link #getConnection(Properties)}.
     * @throws IOException If an error occured while reading from {@code configFileStream}.
     * @throws SQLException If the database connection fails
     */
    public static DatabaseConnection loadFromFile(InputStream configFileStream) throws IOException, SQLException {
        Properties config = new Properties();
        config.load(configFileStream);
        return getConnection(config);
    }

    
    /** 
     * <p>Uses the data in {@code dbConfig} to establish a single connection with the {@link DatabaseDriver}<br> (Will not establish a new connection even if the properties are changed).
     * 
     * @param dbConfig the configuration to use for the connection:
     * @return The instance of {@link #DatabaseConnection(Connection)} for this java application.
     * @throws NumberFormatException If the {@code db.port} value is not a number
     * @throws SQLException If the database connection fails
     */
    public static DatabaseConnection getConnection(Properties dbConfig) throws NumberFormatException, SQLException{
        if (instance == null) {
            String portString = dbConfig.getProperty("db.port"),
                name = dbConfig.getProperty("db.name"),
                user = dbConfig.getProperty("db.user");
            int port = Integer.parseInt(portString);
            String url = String.format(URL_TEMPLATE, dbConfig.getProperty("db.url"), port, name);
            instance = new DatabaseConnection(
                DriverManager.getConnection(url, user, dbConfig.getProperty("db.password"))
            );
        }
        return instance;
    }

    /**
     * Creates a {@link PreparedStatement} from the given {@code query}.
     * 
     * @param query a valid SQL statement.
     * @return the created {@link PreparedStatement}.
     */
    public PreparedStatement prepare(String query) throws SQLException {
        return connection.prepareStatement(query);
    }
}
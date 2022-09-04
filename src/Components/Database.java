package Components;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Represents a DataSource that reads and writes to a single file.
 */
public class Database {
    private Connection connection;
    private static final String URL_TEMPLATE = "jdbc:mysql://%s:%d/%s";
    private static Database instance;

    private static String getUrlFromProperties(String url, int port, String name) {
        return String.format(URL_TEMPLATE, url, port, name);
    }

    public Database(Connection connection) throws SQLException {
        this.connection = connection;
    }

    /**
     * Reads the properties stored in {@value #DB_PROPERTIES_FILE} if no arguments
     * are supplied to the program on execution.
     * 
     * @param args The command line arguments.
     * @return Properties containing the database configuration.
     * @throws FileNotFoundException If the configuration file cannot be found.
     * @throws IOException           If the configuration file cannot be accessed.
     */
    public static Database loadFromFile(InputStream configFileStream) throws IOException, SQLException {
        Properties config = new Properties();
        config.load(configFileStream);
        return load(config);
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
     * Writes text to the file at {@code sourcePath}.
     * 
     * @param text the data to be written.
     * @return true if writing was successful, false otherwise.
     */
    public PreparedStatement prepare(String query) throws SQLException {
        return connection.prepareStatement(query);
    }
}
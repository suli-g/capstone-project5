package config;

public interface Config extends Queries, UserInterface, Overviews, Menus {
    String DATABASE_URL_TEMPLATE = "jdbc:mysql://%s:%d/%s?useSSL=%s";
    String DB_PROPERTIES_FILE = "db.properties";
}

package config;

public interface Config extends Queries {
    String DATABASE_URL_TEMPLATE = "jdbc:mysql://%s:%d/%s?useSSL=%s";
    String DB_PROPERTIES_FILE = "db.properties";
    String MAIN_MENU_NAME = "Main";
    String PROJECT_MENU_NAME = "Project";
    String PERSON_MENU_NAME = "Personnel";
}

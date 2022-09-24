package Model.EntityModel;

import java.sql.SQLException;

import Components.QueryBuilder;
import Model.DatabaseConnection;


public class EntityModel extends EntityUpdater {
    private static EntityModel modelInstance;

    
    /** 
    * Creates a new EntityModel instance if no instance exists in this application;
     * gets the existing instance if an it already already exists.
     * 
     * @param dbInstance an instance of {@link DatabaseConnection}
     * @return the EntityModel instance for the application.
     * @throws SQLException if a database error occurs.
     */
    public static EntityModel getInstance(DatabaseConnection dbInstance) throws SQLException {
        if (modelInstance == null) {
            QueryBuilder qb = QueryBuilder.getInstance(dbInstance);
            modelInstance = new EntityModel(qb);
        }
        return modelInstance;
    }

    /**
     * The constructor for this class.
     * 
     * @param queryBuilder to be used to create and execute queries.
     */
    private EntityModel(QueryBuilder queryBuilder) {
        super(queryBuilder);
    }
}

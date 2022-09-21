package Model.EntityModel;

import java.sql.SQLException;


import Components.QueryBuilder;
import Model.DatabaseModel;

public class EntityModel extends EntityUpdater {
    private static EntityModel modelInstance;
 
    public static EntityModel getInstance(DatabaseModel db) throws SQLException {
        if (modelInstance == null) {
            QueryBuilder qb = QueryBuilder.getInstance(db);
            modelInstance = new EntityModel(qb);
        }
        return modelInstance;
    }

    private EntityModel(QueryBuilder queryBuilder) {
        super(queryBuilder);
    }
}

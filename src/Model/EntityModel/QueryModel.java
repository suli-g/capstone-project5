package Model.EntityModel;

import Components.QueryBuilder;
import Config.Queries;

public abstract class QueryModel implements Queries{
    protected QueryBuilder queryBuilder;
    public QueryModel(QueryBuilder qb) {
        queryBuilder = qb;
    }
}

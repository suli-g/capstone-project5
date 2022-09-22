package Model.EntityModel;

import Components.QueryBuilder;
import Interfaces.Queries;

public abstract class QueryModel implements Queries{
    protected QueryBuilder queryBuilder;
    public QueryModel(QueryBuilder qb) {
        queryBuilder = qb;
    }
}

package Models.EntityModel;

import Components.QueryBuilder;
import Interfaces.IQuery;

public abstract class QueryModel implements IQuery{
    protected QueryBuilder queryBuilder;
    public QueryModel(QueryBuilder qb) {
        queryBuilder = qb;
    }
}

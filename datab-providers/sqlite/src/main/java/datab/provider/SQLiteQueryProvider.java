package datab.provider;

import datab.query.SQLQuery;
import datab.query.SQLiteQuery;

public class SQLiteQueryProvider implements SQLQueryProvider {
    @Override
    public SQLiteQuery.Builder getSQLQueryBuilder() {
        return new SQLiteQuery.Builder();
    }

    @Override
    public SQLiteQuery.Chain.Builder getSQLQueryChainBuilder() {
        return new SQLiteQuery.Chain.Builder();
    }

    @Override
    public SQLiteQuery.Condition.Builder getSQLQueryConditionBuilder() {
        return new SQLiteQuery.Condition.Builder();
    }
}

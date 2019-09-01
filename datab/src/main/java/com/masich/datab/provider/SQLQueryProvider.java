package datab.provider;

import datab.query.SQLQuery;

public interface SQLQueryProvider {
    SQLQuery.Builder getSQLQueryBuilder();

    SQLQuery.Chain.Builder getSQLQueryChainBuilder();

    SQLQuery.Condition.Builder getSQLQueryConditionBuilder();
}

package datab.provider;

import com.masich.datab.provider.SQLQueryProvider;
import com.masich.datab.query.SQLQuery;
import datab.query.SQLiteQueryBuilder;

public class SQLiteQueryProvider implements SQLQueryProvider {
    @Override
    public SQLiteQueryBuilder getSQLQueryBuilder() {
        return new SQLiteQueryBuilder();
    }

    @Override
    public SQLQuery.Chain.Builder getSQLQueryChainBuilder() {
        return new SQLQuery.Chain.Builder();
    }

    @Override
    public SQLQuery.Condition.Builder getSQLQueryConditionBuilder() {
        return new SQLQuery.Condition.Builder();
    }
}

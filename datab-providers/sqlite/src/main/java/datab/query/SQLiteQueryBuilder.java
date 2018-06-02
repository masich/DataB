package datab.query;

public class SQLiteQueryBuilder extends SQLQuery.Builder {
    public SQLiteQueryBuilder createTableIfNotExists(String tableName) {
        this.appendQuery("CREATE TABLE IF NOT EXISTS").appendQuery(tableName);
        return this;
    }

    public SQLiteQueryBuilder dropTableIfExists(String tableName) {
        this.appendQuery("DROP TABLE IF EXISTS").appendQuery(tableName);
        return this;
    }
}

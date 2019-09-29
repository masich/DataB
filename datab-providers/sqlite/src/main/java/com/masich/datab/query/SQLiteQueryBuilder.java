package com.masich.datab.query;

public class SQLiteQueryBuilder extends SQLQuery.Builder {
    public SQLiteQueryBuilder createTableIfNotExists(String tableName) {
        this.appendRawQuery("CREATE TABLE IF NOT EXISTS").appendRawQuery(tableName);
        return this;
    }

    public SQLiteQueryBuilder dropTableIfExists(String tableName) {
        this.appendRawQuery("DROP TABLE IF EXISTS").appendRawQuery(tableName);
        return this;
    }
}

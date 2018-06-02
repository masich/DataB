package datab.query;

public class SQLiteQuery extends SQLQuery {
    public SQLiteQuery() {
        super();
    }

    public SQLiteQuery(String rawString) {
        super(rawString);
    }

    public static class Builder extends SQLQuery.Builder {
        public Builder createTableIfNotExists(String tableName) {
            this.appendQuery("CREATE TABLE IF NOT EXISTS").appendQuery(tableName);
            return this;
        }
    }
}

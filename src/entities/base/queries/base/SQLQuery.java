package entities.base.queries.base;

public abstract class SQLQuery {
    private String rawString;

    public SQLQuery(String rawString) {
        this.rawString = rawString;
    }

    public String toRawString() {
        return rawString;
    }

    public abstract static class Builder<T extends SQLQuery> {
        protected StringBuffer rawQuery;

        public Builder() {
            this.rawQuery = new StringBuffer();
        }

        public Builder(String initialQuery) {
            this.rawQuery = new StringBuffer(initialQuery);
        }

        public abstract T build();
    }


    public static abstract class QueryPart extends SQLQuery {
        public QueryPart(String rawString) {
            super(rawString);
        }

        public static abstract class Builder<T extends QueryPart> extends SQLQuery.Builder<T> {
            public Builder() {
                super();
            }

            public Builder(String initialPart) {
                super(initialPart);
            }
        }
    }
}

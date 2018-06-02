package datab.query;

public abstract class Query {
    private String rawString;

    public Query(String rawString) {
        this.rawString = rawString;
    }

    public String toRawString() {
        return rawString;
    }

    public abstract static class Builder<T extends Query> {
        protected StringBuffer rawQuery;

        public Builder() {
            this.rawQuery = new StringBuffer();
        }

        public Builder(String initialQuery) {
            this.rawQuery = new StringBuffer(initialQuery);
        }

        public abstract T build();
    }


    public static abstract class QueryPart extends Query {
        public QueryPart(String rawString) {
            super(rawString);
        }

        public static abstract class Builder<T extends QueryPart> extends Query.Builder<T> {
            public Builder() {
                super();
            }

            public Builder(String initialPart) {
                super(initialPart);
            }
        }
    }
}

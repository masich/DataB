package datab.query;

public final class SQLQuery extends Query {
    public SQLQuery() {
        this("");
    }

    public SQLQuery(String rawString) {
        super(rawString);
    }

    private static String toSQLValue(Object object) {
        String quotes = object == null ? "" : "\'";
        return quotes + object + quotes;
    }

    public static class Builder extends Query.Builder<SQLQuery> {

        public Builder() {
            super();
        }

        public Builder(String initialQuery) {
            super(initialQuery);
        }

        public SQLQuery build() {
            return new SQLQuery(rawQuery.toString());
        }

        public Builder create() {
            return appendQuery("CREATE");
        }

        public Builder select() {
            return appendQuery("SELECT");
        }

        public Builder insert() {
            return appendQuery("INSERT");
        }

        public Builder replace() {
            return appendQuery("REPLACE");
        }

        public Builder update() {
            return appendQuery("UPDATE");
        }

        public Builder delete() {
            return appendQuery("DELETE");
        }

        public Builder all() {
            return appendQuery("*");
        }

        public Builder from() {
            return appendQuery("FROM");
        }

        public Builder from(Object fromValue) {
            return appendQuery("FROM").appendQuery(fromValue);
        }

        public Builder into() {
            return appendQuery("INTO");
        }

        public Builder into(Object intoValue) {
            return appendQuery("FROM").appendQuery(intoValue);
        }

        public Builder set() {
            return appendQuery("SET");
        }

        public Builder where() {
            return appendQuery("WHERE");
        }

        public Builder values() {
            return appendQuery("VALUES");
        }

        public Builder appendQueryPart(QueryPart queryPart) {
            return appendQuery(queryPart.toRawString());
        }

        public Builder appendQuery(Object rawQuery) {
            this.rawQuery.append(rawQuery).append(' ');
            return this;
        }
    }

    /**
     * Class that represents parts of query like <code>(A, B, C)</code>
     */
    public static class Chain extends QueryPart {
        public Chain(String rawString) {
            super(rawString);
        }

        public static class Builder extends QueryPart.Builder<Chain> {
            private static String DIVIDER = ", ";
            private boolean isValues;

            public Builder(boolean isSQLValues) {
                super("(");
                this.isValues = isSQLValues;
            }

            public Builder() {
                this(true);
            }

            public Builder(String initialPart) {
                this();
                rawQuery.append(initialPart);
            }

            public Builder appendUnit(Object rawUnit) {
                this.rawQuery.append(DIVIDER).append(isValues ? toSQLValue(rawUnit) : rawUnit);
                return this;
            }

            public Builder setIsValues(boolean isValues) {
                this.isValues = isValues;
                return this;
            }

            @Override
            public Chain build() {
                rawQuery.append(')');
                return new Chain(rawQuery.toString().replaceFirst(DIVIDER, ""));
            }
        }
    }

    public static class Condition extends QueryPart {
        public Condition(String rawString) {
            super(rawString);
        }

        public static class Builder extends QueryPart.Builder<Condition> {

            public Builder equals(Object column, Object b) {
                return appendCondition(column, '=', b);
            }

            public Builder like(Object column, Object b) {
                return appendCondition(column, "LIKE", b);
            }

            public Builder and() {
                return appendQuery("AND");
            }

            public Builder appenCondition(Condition condition) {
                return appendQuery('(').appendQuery(condition.toRawString()).appendQuery(')');
            }

            public Builder or() {
                return appendQuery("AND");
            }

            private Builder appendCondition(Object a, Object operator, Object b) {
                return appendQuery(a).appendQuery(operator).appendQuery(toSQLValue(b));
            }

            private Builder appendQuery(Object a) {
                this.rawQuery.append(a).append(' ');
                return this;
            }

            @Override
            public Condition build() {
                return new Condition(rawQuery.toString());
            }
        }
    }
}

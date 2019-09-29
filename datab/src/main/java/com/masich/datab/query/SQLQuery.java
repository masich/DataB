package com.masich.datab.query;

import java.util.ArrayList;
import java.util.List;

public final class SQLQuery implements Query {
    private String rawQuery;
    private List<Object> queryParams;

    public static final SQLQuery EMPTY_QUERY = new SQLQuery("", new ArrayList<Object>());

    public SQLQuery(String rawQuery, List<Object> queryParams) {
        this.rawQuery = rawQuery;
        this.queryParams = queryParams;
    }

    @Override
    public String getRawQuery() {
        return rawQuery;
    }

    @Override
    public List<Object> getQueryParams() {
        return queryParams;
    }

    public static class Builder extends Query.Builder<SQLQuery> {
        private StringBuffer rawQuery;
        private List<Object> queryParams;

        public Builder() {
            rawQuery = new StringBuffer();
            queryParams = new ArrayList<>();
        }

        public SQLQuery build() {
            return new SQLQuery(rawQuery.toString(), queryParams);
        }

        public Builder create() {
            return appendRawQuery("CREATE");
        }

        public Builder select() {
            return appendRawQuery("SELECT");
        }

        public Builder insert() {
            return appendRawQuery("INSERT");
        }

        public Builder replace() {
            return appendRawQuery("REPLACE");
        }

        public Builder update() {
            return appendRawQuery("UPDATE");
        }

        public Builder delete() {
            return appendRawQuery("DELETE");
        }

        public Builder all() {
            return appendRawQuery("*");
        }

        private Builder from() {
            return appendRawQuery("FROM");
        }

        public Builder from(String fromValue) {
            return from().appendRawQuery(fromValue);
        }

        private Builder into() {
            return appendRawQuery("INTO");
        }

        public Builder into(String intoValue) {
            return into().appendRawQuery(intoValue);
        }

        public Builder set() {
            return appendRawQuery("SET");
        }

        public Builder where() {
            return appendRawQuery("WHERE");
        }

        public Builder values() {
            return appendRawQuery("VALUES");
        }

        public Builder appendQueryPart(Query queryPart) {
            appendRawQuery(queryPart.getRawQuery());
            queryParams.addAll(queryPart.getQueryParams());
            return this;
        }

        public Builder appendRawQuery(Object rawQuery) {
            this.rawQuery.append(rawQuery).append(' ');
            return this;
        }

        public Builder appendParams(Object param) {
            appendRawQuery('?');
            queryParams.add(param);
            return this;
        }
    }

    /**
     * Class that represents parts of the query like <code>(A, B, C)</code>
     */
    public static class Chain implements Query {
        private String rawQuery;
        private List<Object> queryParams;

        public Chain(String rawQuery, List<Object> queryParams) {
            this.rawQuery = rawQuery;
            this.queryParams = queryParams;
        }

        @Override
        public String getRawQuery() {
            return rawQuery;
        }

        @Override
        public List<Object> getQueryParams() {
            return queryParams;
        }

        public static class Builder extends Query.Builder<Chain> {
            private static String SEPARATOR = ", ";
            private boolean isValues;

            public Builder(boolean isSQLValues) {
                rawQuery = new StringBuffer();
                queryParams = new ArrayList<>();
                rawQuery.append('(');
                isValues = isSQLValues;
            }

            public Builder() {
                this(true);
            }

            // FIXME: 29.09.19 Remove isValues
            public Builder appendUnit(Object rawUnit) {
                rawQuery.append(SEPARATOR);
                if (isValues) {
                    rawQuery.append('?');
                    queryParams.add(rawUnit);
                } else {
                    rawQuery.append(rawUnit);
                }
                return this;
            }

            public Builder setIsValues(boolean isValues) {
                this.isValues = isValues;
                return this;
            }

            @Override
            public Chain build() {
                rawQuery.append(')');
                return new Chain(rawQuery.toString().replaceFirst(SEPARATOR, ""), queryParams);
            }
        }
    }

    public static class Condition implements Query {
        private String rawQuery;
        private List<Object> queryParams;

        public Condition(String rawQuery, List<Object> queryParams) {
            this.rawQuery = rawQuery;
            this.queryParams = queryParams;
        }

        @Override
        public String getRawQuery() {
            return rawQuery;
        }

        @Override
        public List<Object> getQueryParams() {
            return queryParams;
        }

        public static class Builder extends Query.Builder<Condition> {
            public Builder equals(Object column, Object b) {
                return appendCondition(column, '=', b);
            }

            public Builder like(Object column, Object b) {
                return appendCondition(column, "LIKE", b);
            }

            public Builder and() {
                return appendRawQuery("AND");
            }

            public Builder appendCondition(Condition condition) {
                return appendRawQuery('(').appendQueryPart(condition).appendRawQuery(')');
            }

            public Builder appendQueryPart(Query queryPart) {
                appendRawQuery(queryPart.getRawQuery());
                queryParams.addAll(queryPart.getQueryParams());
                return this;
            }

            public Builder or() {
                return appendRawQuery("OR");
            }

            private Builder appendCondition(Object a, Object operator, Object b) {
                return appendRawQuery(a).appendRawQuery(operator).appendParams(b);
            }

            private Builder appendRawQuery(Object a) {
                this.rawQuery.append(a).append(' ');
                return this;
            }

            public Builder appendParams(Object param) {
                appendRawQuery('?');
                queryParams.add(param);
                return this;
            }

            @Override
            public Condition build() {
                return new Condition(rawQuery.toString(), queryParams);
            }
        }
    }
}

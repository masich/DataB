package com.masich.datab.query;

import java.util.ArrayList;
import java.util.List;

public interface Query {
    String getRawQuery();

    List<Object> getQueryParams();

    abstract class Builder<T extends Query> {
        protected StringBuffer rawQuery;
        protected List<Object> queryParams;

        public Builder() {
            rawQuery = new StringBuffer();
            queryParams = new ArrayList<>();
        }

        public abstract T build();
    }
}

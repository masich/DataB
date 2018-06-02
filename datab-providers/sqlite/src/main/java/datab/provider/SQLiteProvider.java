package datab.provider;

import datab.Entity;
import datab.provider.datatype.SQLFieldAttributes;
import datab.provider.datatype.SQLiteAttributesConverter;
import datab.query.SQLQuery;
import datab.utils.ReflectionUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteProvider implements DBProvider {
    private SQLiteQueryProvider sqlQueryProvider;

    @Override
    public String getDBStringPrefix() {
        return "jdbc:sqlite:";
    }

    @Override
    public void initDB(Connection connection, Iterable<Class<? extends Entity>> entityClasses) throws SQLException {
        for (Class<? extends Entity> entityClass : entityClasses) {
            createTableIfNotExists(connection, entityClass);
        }
    }

    @Override
    public void dropDB(Connection connection, Iterable<Class<? extends Entity>> entityClasses) throws SQLException {
        for (Class<? extends Entity> entityClass : entityClasses) {
            dropTableIfExists(connection, entityClass);
        }
    }

    @Override
    public Connection getDbConnection(String dbSrc) throws SQLException {
        return DriverManager.getConnection(dbStringFromSource(dbSrc));
    }

    @Override
    public Connection getDbConnection(String dbSrc, String login, String password) throws SQLException {
        return DriverManager.getConnection(dbStringFromSource(dbSrc), login, password);
    }

    @Override
    public SQLiteQueryProvider getSQLQueryProvider() {
        if (sqlQueryProvider == null) {
            sqlQueryProvider = new SQLiteQueryProvider();
        }
        return sqlQueryProvider;
    }

    private String dbStringFromSource(String dbSrc) {
        if (dbSrc.startsWith(getDBStringPrefix()))
            return dbSrc;
        return getDBStringPrefix() + dbSrc;
    }

    void createTableIfNotExists(Connection connection, Class<? extends Entity> entityClass) throws SQLException {
        SQLiteQueryProvider queryProvider = getSQLQueryProvider();
        SQLQuery.Chain.Builder chainBuilder = queryProvider.getSQLQueryChainBuilder().setIsValues(false);
        SQLiteAttributesConverter converter = new SQLiteAttributesConverter();

        for (SQLFieldAttributes attributes : converter.fromClassesFields(ReflectionUtils.getAllFields(entityClass))) {
            chainBuilder.appendUnit(getFullDescription(attributes));
        }

        SQLQuery.Builder queryBuilder = queryProvider.getSQLQueryBuilder()
                .createTableIfNotExists(ReflectionUtils.getTableName(entityClass))
                .appendQueryPart(chainBuilder.build());

        connection.createStatement().execute(queryBuilder.build().toRawString());
    }

    void dropTableIfExists(Connection connection, Class<? extends Entity> entityClass) throws SQLException {
        SQLiteQueryProvider queryProvider = getSQLQueryProvider();
        SQLQuery.Builder queryBuilder = queryProvider.getSQLQueryBuilder()
                .dropTableIfExists(ReflectionUtils.getTableName(entityClass));

        connection.createStatement().execute(queryBuilder.build().toRawString());
    }

    String getFullDescription(SQLFieldAttributes attributes) {
        return attributes.getSqlName() + " " + attributes.getSQLDataType().getSQLName()
                + (attributes.getSQLSense() == SQLFieldAttributes.Sense.PRIMARY_KEY ? "PRIMARY KEY" : "");
    }
}
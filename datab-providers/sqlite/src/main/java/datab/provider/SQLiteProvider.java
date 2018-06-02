package datab.provider;

import datab.Entity;
import datab.exception.FieldNotFoundException;
import datab.query.SQLQuery;
import datab.utils.FieldAttributes;
import datab.utils.JavaDataTypes;
import datab.utils.ReflectionUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteProvider implements Provider {
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
        SQLQueryProvider queryProvider = getSQLQueryProvider();
        SQLQuery.Chain.Builder chainBuilder = queryProvider.getSQLQueryChainBuilder().setIsValues(false);
        for (FieldAttributes attributes : ReflectionUtils.getAllFieldsAttributes(entityClass)) {
            chainBuilder.appendUnit(attributes.getSqlName() + " " + getDataTypeFor(attributes.getType()).getName()
                    + (attributes.getDescription().equals("PRIMARY KEY") ? " " + attributes.getDescription() : ""));
        }

        SQLQuery.Builder queryBuilder = queryProvider.getSQLQueryBuilder()
                .appendQuery("CREATE TABLE IF NOT EXISTS")
                .appendQuery(ReflectionUtils.getTableName(entityClass))
                .appendQueryPart(chainBuilder.build());

        connection.createStatement().execute(queryBuilder.build().toRawString());
    }

    DataType getDataTypeFor(Class entityField) {
        if (Entity.class.isAssignableFrom(entityField)) {
            try {
                return getDataTypeFor(ReflectionUtils.getFieldType(ReflectionUtils.getPrimaryKeyField(entityField)));
            } catch (FieldNotFoundException e) {
                e.printStackTrace();
                return DataType.TEXT;
            }
        } else if (JavaDataTypes.getIntTypes().contains(entityField)) {
            return DataType.INTEGER;
        } else if (JavaDataTypes.getFloatTypes().contains(entityField)) {
            return DataType.REAL;
        } else {
            return DataType.TEXT;
        }
    }
}

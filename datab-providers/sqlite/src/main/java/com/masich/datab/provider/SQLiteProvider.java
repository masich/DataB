package com.masich.datab.provider;

import com.masich.datab.provider.attributes.ColumnAttributes;
import com.masich.datab.provider.attributes.TableAttributes;
import com.masich.datab.provider.datatype.*;
import com.masich.datab.query.Query;
import com.masich.datab.query.SQLQuery;
import org.sqlite.JDBC;

import java.sql.*;

public class SQLiteProvider implements DatabaseProvider {
    private SQLiteQueryProvider sqlQueryProvider;
    private SQLiteDataTypeConverter dataTypeConverter;

    static {
        try {
            DriverManager.registerDriver(new JDBC());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDatabaseStringPrefix() {
        return JDBC.PREFIX;
    }

    @Override
    public void initDatabase(Connection dbConnection, Iterable<TableAttributes> tablesAttributes) throws SQLException {
        for (TableAttributes tableAttributes : tablesAttributes) {
            createTableIfNotExists(dbConnection, tableAttributes);
        }
    }

    @Override
    public void dropDatabase(Connection dbConnection, Iterable<TableAttributes> tablesAttributes) throws SQLException {
        for (TableAttributes tableAttributes : tablesAttributes) {
            dropTableIfExists(dbConnection, tableAttributes.getTableName());
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

    @Override
    public DataType.Converter getDataTypeConverter() {
        if (dataTypeConverter == null) {
            dataTypeConverter = new SQLiteDataTypeConverter();
        }
        return dataTypeConverter;
    }

    private PreparedStatement getPreparedStatement(Connection connection, Query query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query.getRawQuery());
        int paramIndex = 1;
        for (Object param : query.getQueryParams()) {
            statement.setObject(paramIndex++, param);
        }
        return statement;
    }

    @Override
    public ResultSet executeQueryUnsafe(Connection connection, String query) throws SQLException {
        return connection.createStatement().executeQuery(query);
    }

    @Override
    public ResultSet executeQuery(Connection connection, Query query) throws SQLException {
        PreparedStatement statement = getPreparedStatement(connection, query);
        return statement.executeQuery();
    }

    @Override
    public Integer executeUpdateUnsafe(Connection connection, String query) throws SQLException {
        return connection.createStatement().executeUpdate(query);
    }

    @Override
    public Integer executeUpdate(Connection connection, Query query) throws SQLException {
        PreparedStatement statement = getPreparedStatement(connection, query);

        return statement.executeUpdate();
    }

    private String dbStringFromSource(String dbSrc) {
        if (dbSrc.startsWith(getDatabaseStringPrefix()))
            return dbSrc;
        return getDatabaseStringPrefix() + dbSrc;
    }

    private void createTableIfNotExists(Connection connection, TableAttributes tableAttributes) throws SQLException {
        SQLiteQueryProvider queryProvider = getSQLQueryProvider();
        // TODO: 29.09.19 Remove isValue
        SQLQuery.Chain.Builder chainBuilder = queryProvider.getSQLQueryChainBuilder().setIsValues(false);

        for (ColumnAttributes attributes : tableAttributes.getColumnAttributes()) {
            chainBuilder.appendUnit(getFullDescription(attributes));
        }

        SQLQuery query = queryProvider.getSQLQueryBuilder()
                .createTableIfNotExists(tableAttributes.getTableName())
                .appendQueryPart(chainBuilder.build()).build();

        connection.createStatement().execute(query.getRawQuery());
    }

    // FIXME: 29.09.19 Check SQLQueryBuilder
    private void dropTableIfExists(Connection connection, String tableName) throws SQLException {
        SQLiteQueryProvider queryProvider = getSQLQueryProvider();
        SQLQuery.Builder queryBuilder = queryProvider.getSQLQueryBuilder()
                .dropTableIfExists(tableName);

        connection.createStatement().execute(queryBuilder.build().getRawQuery());
    }

    private String getFullDescription(ColumnAttributes attributes) {
        DataType dataType = getDataTypeConverter().fromInternalType(attributes.getInternalDataType());
        return attributes.getColumnName() + " " + dataType.getSQLName()
                + (attributes.getSQLSense() == ColumnAttributes.FieldSQLSense.PRIMARY_KEY ? " PRIMARY KEY" : "");
    }
}

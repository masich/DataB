package com.masich.datab.provider;

import com.masich.datab.provider.attributes.ColumnAttributes;
import com.masich.datab.provider.attributes.TableAttributes;
import com.masich.datab.provider.datatype.*;
import com.masich.datab.query.SQLQuery;
import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteProvider implements DBProvider {
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
    public String getDBStringPrefix() {
        return JDBC.PREFIX;
    }

    @Override
    public void initDB(Connection dbConnection, Iterable<TableAttributes> tablesAttributes) throws SQLException {
        for (TableAttributes tableAttributes : tablesAttributes) {
            createTableIfNotExists(dbConnection, tableAttributes);
        }
    }

    @Override
    public void dropDB(Connection dbConnection, Iterable<TableAttributes> tablesAttributes) throws SQLException {
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

    private String dbStringFromSource(String dbSrc) {
        if (dbSrc.startsWith(getDBStringPrefix()))
            return dbSrc;
        return getDBStringPrefix() + dbSrc;
    }

    void createTableIfNotExists(Connection connection, TableAttributes tableAttributes) throws SQLException {
        SQLiteQueryProvider queryProvider = getSQLQueryProvider();
        SQLQuery.Chain.Builder chainBuilder = queryProvider.getSQLQueryChainBuilder().setIsValues(false);

        for (ColumnAttributes attributes : tableAttributes.getColumnAttributes()) {
            chainBuilder.appendUnit(getFullDescription(attributes));
        }

        SQLQuery query = queryProvider.getSQLQueryBuilder()
                .createTableIfNotExists(tableAttributes.getTableName())
                .appendQueryPart(chainBuilder.build()).build();

        connection.createStatement().execute(query.toRawString());
    }

    void dropTableIfExists(Connection connection, String tableName) throws SQLException {
        SQLiteQueryProvider queryProvider = getSQLQueryProvider();
        SQLQuery.Builder queryBuilder = queryProvider.getSQLQueryBuilder()
                .dropTableIfExists(tableName);

        connection.createStatement().execute(queryBuilder.build().toRawString());
    }

    String getFullDescription(ColumnAttributes attributes) {
        DataType dataType = getDataTypeConverter().fromInternalType(attributes.getInternalDataType());
        return attributes.getColumnName() + " " + dataType.getSQLName()
                + (attributes.getSQLSense() == ColumnAttributes.FieldSQLSense.PRIMARY_KEY ? " PRIMARY KEY" : "");
    }
}

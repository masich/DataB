package com.masich.datab.provider;

import com.masich.datab.provider.datatype.DataType;
import com.masich.datab.provider.attributes.TableAttributes;
import com.masich.datab.query.Query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DatabaseProvider {
    /**
     * @return Database scheme prefix in <code>jdbc:DB_SCHEME:</code> format
     */
    String getDatabaseStringPrefix();

    /**
     * Method that initializes particular tables in the database.
     * <p>
     * The implementation of this method should include an initialization of all of the database tables specified by
     * <code>entityClasses</code> data structure.
     *
     * @param dbConnection     JDBC database connection instance
     * @param tablesAttributes A data structure that contains Entity table attributes to initialize in the database instance
     * @throws SQLException An exception that could occur during database initialization
     */
    void initDatabase(Connection dbConnection, Iterable<TableAttributes> tablesAttributes) throws SQLException;

    /**
     * Method that drops particular tables in the database.
     *
     * @param dbConnection     JDBC database connection instance
     * @param tablesAttributes A data structure that contains Entity table attributes to drop in the database instance
     * @throws SQLException An exception that could occur during database drop
     */
    void dropDatabase(Connection dbConnection, Iterable<TableAttributes> tablesAttributes) throws SQLException;

    /**
     * @param dbSrc A database source path
     * @return A JDBC connection
     * @throws SQLException An exception that could occur during connection setup for particular database
     */
    Connection getDbConnection(String dbSrc) throws SQLException;

    /**
     * @param dbSrc    A database source path
     * @param login    Login for connection to a particular database
     * @param password Password for the appropriate login
     * @return A JDBC connection to a particular database
     * @throws SQLException An exception that could occur during connection setup for particular database
     */
    Connection getDbConnection(String dbSrc, String login, String password) throws SQLException;

    /**
     * @return A provider that will be used to construct the SQL queries
     */
    SQLQueryProvider getSQLQueryProvider();

    /**
     * @return A DataType.Converter instance
     */
    DataType.Converter getDataTypeConverter();

    // TODO: 29.09.19 Write documentation
    ResultSet executeQuery(Connection connection, Query query) throws SQLException;

    // TODO: 29.09.19 Write documentation
    Integer executeUpdateUnsafe(Connection connection, String query) throws SQLException;

    // TODO: 29.09.19 Write documentation
    Integer executeUpdate(Connection connection, Query query) throws SQLException;

    // TODO: 29.09.19 Write documentation
    ResultSet executeQueryUnsafe(Connection connection, String query) throws SQLException;

    interface Factory {
        DatabaseProvider getDatabaseProvider();
    }
}

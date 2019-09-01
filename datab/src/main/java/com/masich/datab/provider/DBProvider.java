package com.masich.datab.provider;

import com.masich.datab.Entity;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBProvider {
    /**
     * @return Database scheme prefix in <code>jdbc:DB_SCHEME:</code> format
     */
    String getDBStringPrefix();

    /**
     * Method that initializes particular tables in the database.
     * <p>
     * The implementation of this method should include an initialization of all of the database tables specified by
     * <code>entityClasses</code> data structure.
     *
     * @param dbConnection  JDBC database connection instance
     * @param entityClasses A data structure that contains Entities to initialize in the database instance
     * @throws SQLException An exception that could occur during database initialization
     */
    void initDB(Connection dbConnection, Iterable<Class<? extends Entity>> entityClasses) throws SQLException;

    /**
     * Method that drops particular tables in the database.
     *
     * @param dbConnection  JDBC database connection instance
     * @param entityClasses A data structure that contains Entities to drop in the database instance
     * @throws SQLException An exception that could occur during database drop
     */
    void dropDB(Connection dbConnection, Iterable<Class<? extends Entity>> entityClasses) throws SQLException;

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

    interface Factory {
        DBProvider getDBProvider();
    }
}

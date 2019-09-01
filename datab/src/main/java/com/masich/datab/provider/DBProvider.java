package datab.provider;

import datab.Entity;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBProvider {
    /**
     * @return DataBase scheme prefix in format <code>jdbc:DB_SCHEME:</code>
     */
    String getDBStringPrefix();

    void initDB(Connection dbConnection, Iterable<Class<? extends Entity>> entityClasses) throws SQLException;

    void dropDB(Connection dbConnection, Iterable<Class<? extends Entity>> entityClasses) throws SQLException;

    Connection getDbConnection(String dbSrc) throws SQLException;

    Connection getDbConnection(String dbSrc, String login, String password) throws SQLException;

    SQLQueryProvider getSQLQueryProvider();

    interface Factory {
        DBProvider getDBProvider();
    }
}

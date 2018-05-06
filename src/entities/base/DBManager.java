package entities.base;

import entities.base.converters.base.Converter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private Connection dbConnection;
    private Converter converter;

    public DBManager(String dbSrc) throws SQLException {
        dbConnection = DriverManager.getConnection(dbSrc);

    }

    public Connection getConnection() {
        return dbConnection;
    }

    public DBManager setConverter(Converter converter) {
        this.converter = converter;
        return this;
    }

    public Converter getConverter() {
        return converter;
    }

    public void close() throws SQLException {
        dbConnection.close();
    }

    public void beginTransaction() throws SQLException {
        dbConnection.setAutoCommit(false);
    }

    public void finishTransaction() throws SQLException {
        dbConnection.commit();
        dbConnection.setAutoCommit(true);
    }

    public void checkForeignKey(boolean check) throws SQLException {
        dbConnection.createStatement()
                .execute("SET FOREIGN_KEY_CHECKS = " + (check ? "1" : "0"));
    }

    private static DBManager managerInstance;

    public static DBManager getSingleton() {
        return managerInstance;
    }

    public static void setSingleton(DBManager dbManager) {
        managerInstance = dbManager;
    }


    public static class Builder {
        private DBManager dbManager;

        public Builder addDatabaseSrc(String dbSrc) throws SQLException {
            this.dbManager = new DBManager(dbSrc);
            return this;
        }

        public Builder addConverter(Converter converter) {
            this.dbManager.converter = converter;
            return this;
        }

        public DBManager build() {
            return dbManager;
        }
    }
}

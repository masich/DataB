package datab;

import datab.converter.Converter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private Connection dbConnection;
    private Converter.Factory converterFactory;

    public DBManager(String dbSrc) throws SQLException {
        dbConnection = DriverManager.getConnection(dbSrc);
    }

    public Connection getConnection() {
        return dbConnection;
    }

    public void setConverterFactory(Converter.Factory converterFactory) {
        this.converterFactory = converterFactory;
    }

    public Converter.Factory getConverterFactory() {
        return converterFactory;
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

        public Builder addConverterFactory(Converter.Factory converterFactory) {
            this.dbManager.converterFactory = converterFactory;
            return this;
        }

        public DBManager build() {
            return dbManager;
        }
    }
}

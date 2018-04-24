package entities.base;

import test.KeyStore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private Connection dbConnection;

    private DBManager(String dbSrc) throws SQLException {
        dbConnection = DriverManager.getConnection(dbSrc);
    }

    public Connection getConnection() {
        return dbConnection;
    }

    public void close() throws SQLException {
        dbConnection.close();
    }

    private static DBManager managerInstance;

    public static DBManager getInstance() throws SQLException {
        if (managerInstance == null) {
            managerInstance = new DBManager("jdbc:mysql://localhost/" + KeyStore.DB_NAME + "?" +
                    "user=" + KeyStore.LOGIN + "&password=" + KeyStore.PASSWORD);
        }
        return managerInstance;
    }
}

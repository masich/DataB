package entities.base;

import info.KeyStore;

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

    //Fixme
    public static DBManager getInstance() throws SQLException {
        if (managerInstance == null) {
            managerInstance = new DBManager("jdbc:mysql://localhost/" + KeyStore.DB_NAME + "?" +
                    "user=" + KeyStore.LOGIN + "&password=" + KeyStore.PASSWORD);
        }
        return managerInstance;
    }
}

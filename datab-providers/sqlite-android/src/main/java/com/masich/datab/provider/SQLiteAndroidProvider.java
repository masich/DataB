package com.masich.datab.provider;

import org.sqldroid.SQLDroidDriver;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteAndroidProvider extends SQLiteProvider {
    static {
        try {
            DriverManager.registerDriver(new SQLDroidDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDatabaseStringPrefix() {
        return SQLDroidDriver.sqldroidPrefix;
    }
}

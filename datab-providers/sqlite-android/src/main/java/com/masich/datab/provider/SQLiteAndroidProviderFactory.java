package com.masich.datab.provider;

public class SQLiteAndroidProviderFactory extends SQLiteProviderFactory {
    @Override
    public DatabaseProvider getDatabaseProvider() {
        if (DatabaseProvider == null)
            DatabaseProvider = new SQLiteAndroidProvider();
        return DatabaseProvider;
    }
}

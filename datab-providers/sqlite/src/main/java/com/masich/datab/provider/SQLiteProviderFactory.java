package com.masich.datab.provider;

public class SQLiteProviderFactory implements DatabaseProvider.Factory {
    protected DatabaseProvider DatabaseProvider;

    @Override
    public DatabaseProvider getDatabaseProvider() {
        if (DatabaseProvider == null)
            DatabaseProvider = new SQLiteProvider();
        return DatabaseProvider;
    }
}

package com.masich.datab.provider;

public class SQLiteAndroidProviderFactory extends SQLiteProviderFactory {
    @Override
    public com.masich.datab.provider.DBProvider getDBProvider() {
        if (DBProvider == null)
            DBProvider = new SQLiteAndroidProvider();
        return DBProvider;
    }
}

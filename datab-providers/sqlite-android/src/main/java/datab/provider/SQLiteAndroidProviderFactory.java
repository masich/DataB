package datab.provider;

public class SQLiteAndroidProviderFactory extends SQLiteProviderFactory {
    @Override
    public DBProvider getDBProvider() {
        if (DBProvider == null)
            DBProvider = new SQLiteAndroidProvider();
        return DBProvider;
    }
}

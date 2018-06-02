package datab.provider;

public class SQLiteProviderFactory implements DBProvider.Factory {
    private DBProvider DBProvider;

    @Override
    public DBProvider getDBProvider() {
        if (DBProvider == null)
            DBProvider = new SQLiteProvider();
        return DBProvider;
    }
}

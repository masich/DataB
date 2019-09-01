package datab.provider;

import com.masich.datab.provider.DBProvider;

public class SQLiteProviderFactory implements com.masich.datab.provider.DBProvider.Factory {
    protected DBProvider DBProvider;

    @Override
    public DBProvider getDBProvider() {
        if (DBProvider == null)
            DBProvider = new SQLiteProvider();
        return DBProvider;
    }
}

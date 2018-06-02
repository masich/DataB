package datab.provider;

public class SQLiteProviderFactory implements Provider.Factory {
    private Provider provider;

    @Override
    public Provider getProvider() {
        if (provider == null)
            provider = new SQLiteProvider();
        return provider;
    }
}

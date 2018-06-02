package datab;

import datab.converter.Converter;
import datab.provider.DBProvider;
import datab.provider.SQLQueryProvider;
import datab.query.SQLQuery;
import datab.utils.ReflectionUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager implements SQLQueryProvider {
    private static DBManager managerInstance;
    private String databaseSrc;
    private String entityPackageName;
    private Connection dbConnection;
    private Converter.Factory converterFactory;
    private DBProvider.Factory providerFactory;

    private DBManager() {
    }

    public DBManager(String entityPackageName, String databaseSrc) throws SQLException {
        this.databaseSrc = databaseSrc;
        this.dbConnection = getNewConnection(databaseSrc);
        this.entityPackageName = entityPackageName;
        initDB();
    }

    public static DBManager getSingleton() {
        return managerInstance;
    }

    public static void setSingleton(DBManager dbManager) {
        managerInstance = dbManager;
    }

    public void initDB() throws SQLException {
        providerFactory.getDBProvider().initDB(getConnection(),
                ReflectionUtils.getAllSubclassesByPackage(entityPackageName, Entity.class));
    }

    public void dropDB() throws SQLException {
        providerFactory.getDBProvider().dropDB(getConnection(),
                ReflectionUtils.getAllSubclassesByPackage(entityPackageName, Entity.class));
    }

    public Connection getConnection() throws SQLException {
        if (dbConnection == null || dbConnection.isClosed())
            dbConnection = getNewConnection(databaseSrc);
        return dbConnection;
    }

    public String getDatabaseSrc() {
        return databaseSrc;
    }

    void setDatabaseSrc(String dbSrc) {
        this.databaseSrc = dbSrc;
    }

    public String getEntityPackageName() {
        return entityPackageName;
    }

    void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    public Converter.Factory getConverterFactory() {
        return converterFactory;
    }

    public void setConverterFactory(Converter.Factory converterFactory) {
        this.converterFactory = converterFactory;
    }

    public DBProvider.Factory getProviderFactory() {
        return providerFactory;
    }

    public void setProviderFactory(DBProvider.Factory providerFactory) {
        this.providerFactory = providerFactory;
    }

    //Todo: remove it
//    public void checkForeignKey(boolean check) throws SQLException {
//        dbConnection.createStatement()
//                .execute("SET FOREIGN_KEY_CHECKS = " + (check ? "1" : "0"));
//    }

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

    private Connection getNewConnection(String dbSrc) throws SQLException {
        return DriverManager.getConnection(dbSrc);
    }

    @Override
    public SQLQuery.Builder getSQLQueryBuilder() {
        return getProviderFactory().getDBProvider().getSQLQueryProvider().getSQLQueryBuilder();
    }

    @Override
    public SQLQuery.Chain.Builder getSQLQueryChainBuilder() {
        return getProviderFactory().getDBProvider().getSQLQueryProvider().getSQLQueryChainBuilder();
    }

    @Override
    public SQLQuery.Condition.Builder getSQLQueryConditionBuilder() {
        return getProviderFactory().getDBProvider().getSQLQueryProvider().getSQLQueryConditionBuilder();
    }

    private Connection getNewConnection(String dbSrc, String login, String password) throws SQLException {
        return DriverManager.getConnection(dbSrc, login, password);
    }

    public static class Builder {
        private DBManager dbManager;

        public Builder() {
            this.dbManager = new DBManager();
        }

        public Builder addDatabaseSrc(String dbSrc) {
            this.dbManager.setDatabaseSrc(dbSrc);
            return this;
        }

        public Builder addPackage(String entityPackageName) {
            this.dbManager.setEntityPackageName(entityPackageName);
            return this;
        }

        public Builder addConverterFactory(Converter.Factory converterFactory) {
            this.dbManager.setConverterFactory(converterFactory);
            return this;
        }

        public Builder addProviderFactory(DBProvider.Factory providerFactory) {
            this.dbManager.setProviderFactory(providerFactory);
            return this;
        }

        public DBManager build() throws SQLException {
            dbManager.initDB();
            return dbManager;
        }
    }
}

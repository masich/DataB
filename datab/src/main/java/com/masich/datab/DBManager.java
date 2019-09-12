package com.masich.datab;

import com.masich.datab.converter.Converter;
import com.masich.datab.provider.DBProvider;
import com.masich.datab.provider.SQLQueryProvider;
import com.masich.datab.provider.attributes.TableAttributes;
import com.masich.datab.query.SQLQuery;
import com.masich.datab.utils.ReflectionUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class DBManager implements SQLQueryProvider, DBProvider.Factory, Converter.Factory {
    private static DBManager managerInstance;
    private String databaseSrc;
    private String entityPackageName = "";
    private Connection dbConnection;
    private Converter.Factory converterFactory;
    private DBProvider.Factory providerFactory;

    private DBManager() {
    }

    public DBManager(String databaseSrc) throws SQLException {
        this("", databaseSrc);
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
        Set<Class<? extends Entity>> entityClasses = ReflectionUtils.getAllSubclassesByPackage(entityPackageName, Entity.class);
        List<TableAttributes> tableAttributesList = TableAttributes.fromEntityClasses(entityClasses);
        providerFactory.getDBProvider().initDB(getConnection(), tableAttributesList);
    }

    public void dropDB() throws SQLException {
        Set<Class<? extends Entity>> entityClasses = ReflectionUtils.getAllSubclassesByPackage(entityPackageName, Entity.class);
        List<TableAttributes> tableAttributesList = TableAttributes.fromEntityClasses(entityClasses);
        providerFactory.getDBProvider().dropDB(getConnection(), tableAttributesList);
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

    public Converter getConverter() {
        return converterFactory.getConverter();
    }

    public void setConverterFactory(Converter.Factory converterFactory) {
        this.converterFactory = converterFactory;
    }

    public void setProviderFactory(DBProvider.Factory providerFactory) {
        this.providerFactory = providerFactory;
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

    private Connection getNewConnection(String dbSrc) throws SQLException {
        return DriverManager.getConnection(dbSrc);
    }

    @Override
    public SQLQuery.Builder getSQLQueryBuilder() {
        return getDBProvider().getSQLQueryProvider().getSQLQueryBuilder();
    }

    @Override
    public SQLQuery.Chain.Builder getSQLQueryChainBuilder() {
        return getDBProvider().getSQLQueryProvider().getSQLQueryChainBuilder();
    }

    @Override
    public SQLQuery.Condition.Builder getSQLQueryConditionBuilder() {
        return getDBProvider().getSQLQueryProvider().getSQLQueryConditionBuilder();
    }

    private Connection getNewConnection(String dbSrc, String login, String password) throws SQLException {
        return DriverManager.getConnection(dbSrc, login, password);
    }

    @Override
    public DBProvider getDBProvider() {
        return providerFactory.getDBProvider();
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
            if (dbManager.providerFactory == null) {
                throw new NullPointerException("ProviderFactory cannot be null. Please, provide it by using addProviderFactory() method");
            }
            String dbPrefix = dbManager.providerFactory.getDBProvider().getDBStringPrefix();
            if (!dbManager.databaseSrc.startsWith(dbPrefix)) {
                dbManager.databaseSrc = dbPrefix + dbManager.databaseSrc;
            }
            dbManager.initDB();
            return dbManager;
        }
    }
}

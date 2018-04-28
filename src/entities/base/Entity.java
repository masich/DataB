package entities.base;

import entities.base.exceptions.FieldNotFoundException;
import entities.base.queries.MySQLQuery;
import entities.base.queries.base.SQLQuery;
import entities.base.utils.ReflectionUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

abstract public class Entity {
    public final void save() {
        save(this);
    }

    /**
     * Deletes an instance from appropriate database table.
     * <p>
     * Deletes an instance with the same primary key from appropriate
     * database table.
     *
     * @return <code>true</code> if entity was successfully deleted, otherwise <code>false</code>.
     */
    public final boolean delete() {
        return delete(this);
    }


    /**
     * Returns all of the <code>entityClass</code> entities from the
     * appropriate database table.
     *
     * @param entityClass class of the database table entity.
     * @param <T>         type of the result value. The type of <code>entityClass</code> in this case.
     * @return all of the entities of the appropriate database table,
     * emptyList if the database table is empty,
     * <code>null</code> if something went wrong.
     */
    public static <T> List<T> getAll(final Class<T> entityClass) {
        try {
            String tableName = ReflectionUtils.getTableName(entityClass);
            SQLQuery query = new MySQLQuery.Builder().select().all().from(tableName).build();
            ResultSet entityResultSet = DBManager.getInstance().getConnection().createStatement()
                    .executeQuery(query.toRawString());
            List<T> allEntities = new ArrayList<>();
            while (entityResultSet.next()) {
                allEntities.add(getEntity(entityClass, entityResultSet));
            }
            return allEntities;
        } catch (Exception e) {
            //Todo: logging an exception
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteAll(final Class<? extends Entity> entityClass) {
        try {
            SQLQuery query = new MySQLQuery.Builder().delete().from(ReflectionUtils.getTableName(entityClass)).build();
            return DBManager.getInstance().getConnection().createStatement()
                    .executeUpdate(query.toRawString()) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void save(final Entity obj) {
        try {
            Connection connection = DBManager.getInstance().getConnection();
            connection.setAutoCommit(false);
            saveRecursively(obj, new HashSet<>());
            connection.setAutoCommit(true);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void saveRecursively(final Object obj, Set<Object> saved) throws SQLException {
        Class<?> entityClass = obj.getClass();
        try {
            SQLQuery query;
            if (!saved.contains(obj)) {
                if (Entity.class.isAssignableFrom(entityClass)) {
                    saved.add(obj);
                    String tableName = ReflectionUtils.getTableName(entityClass);
                    List<java.lang.reflect.Field> entityFields = ReflectionUtils.getAllFields(entityClass);
                    MySQLQuery.Builder queryBuilder = new MySQLQuery.Builder()
                            .replace()
                            .into()
                            .appendQuery(tableName)
                            .values();
                    MySQLQuery.Chain.Builder valuesBuilder = new MySQLQuery.Chain.Builder();
                    for (java.lang.reflect.Field field : entityFields) {
                        if (ReflectionUtils.isField(field)) {
                            valuesBuilder.appendUnit(ReflectionUtils.getFieldValue(field, obj));
                        } else if (ReflectionUtils.isForeignKey(field)) {
                            Object foreignKeyValue = ReflectionUtils.getFieldValue(field, obj);
                            saveRecursively(foreignKeyValue, saved);
                            valuesBuilder.appendUnit(ReflectionUtils.getPrimaryKeyValue(foreignKeyValue));
                        } else if (ReflectionUtils.isPrimaryKey(field)) {
                            valuesBuilder.appendUnit(ReflectionUtils.getFieldValue(field, obj));
                        }
                    }
                    query = queryBuilder.appendQueryPart(valuesBuilder.build()).build();
                } else {
                    //Todo: Serialize another objects
                    query = new MySQLQuery("");
                }
                DBManager.getInstance()
                        .getConnection()
                        .createStatement()
                        .executeUpdate(query.toRawString());
            }
        } catch (FieldNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static <T> T getById(Object id, Class<T> entityClass) {
        try {
            String tableName = ReflectionUtils.getTableName(entityClass);
            java.lang.reflect.Field primaryKeyField = ReflectionUtils.getPrimaryKeyField(entityClass);
            if (primaryKeyField == null) return null;
            String primaryKey = ReflectionUtils.getPrimaryKey(primaryKeyField);
            MySQLQuery.Builder queryBuilder = new MySQLQuery.Builder()
                    .select()
                    .all()
                    .from(tableName)
                    .where();
            MySQLQuery.Condition condition = new MySQLQuery.Condition.Builder()
                    .equals(primaryKey, id)
                    .build();
            SQLQuery query = queryBuilder.appendQueryPart(condition).build();
            ResultSet entityResultSet = DBManager.getInstance().getConnection().createStatement()
                    .executeQuery(query.toRawString());
            if (!entityResultSet.next()) return null;
            return getEntity(entityClass, entityResultSet);
        } catch (Exception e) {
            //Todo: logging an exception
            e.printStackTrace();
        }
        return null;
    }

    public static boolean delete(final Entity obj) {
        try {
            Class<? extends Entity> entityClass = obj.getClass();
            String tableName = ReflectionUtils.getTableName(entityClass);
            java.lang.reflect.Field primaryKeyField = ReflectionUtils.getPrimaryKeyField(entityClass);
            if (primaryKeyField == null) return false;
            String primaryKey = ReflectionUtils.getPrimaryKey(primaryKeyField);
            Object id = ReflectionUtils.getFieldValue(primaryKeyField, obj);
            MySQLQuery.Builder queryBuilder = new MySQLQuery.Builder()
                    .delete()
                    .from(tableName)
                    .where();
            MySQLQuery.Condition condition = new MySQLQuery.Condition.Builder()
                    .equals(primaryKey, id)
                    .build();
            SQLQuery query = queryBuilder.appendQueryPart(condition).build();
            return DBManager.getInstance().getConnection().createStatement()
                    .executeUpdate(query.toRawString()) > 0;
        } catch (Exception e) {
            //Todo: logging an exception
            e.printStackTrace();
            return false;
        }
    }

    private static <T> T getEntity(Class<T> entityClass, ResultSet entityResultSet) {
        T entity = ReflectionUtils.getNewInstance(entityClass);
        List<java.lang.reflect.Field> entityFields = ReflectionUtils.getAllFields(entityClass);
        try {
            for (java.lang.reflect.Field field : entityFields) {
                if (ReflectionUtils.isPrimaryKey(field)) {
                    ReflectionUtils.setFieldValue(field, entity,
                            entityResultSet.getObject(ReflectionUtils.getPrimaryKey(field)));
                } else if (ReflectionUtils.isField(field)) {
                    ReflectionUtils.setFieldValue(field, entity,
                            entityResultSet.getObject(ReflectionUtils.getFieldName(field)));
                } else if (ReflectionUtils.isForeignKey(field)) {
                    ReflectionUtils.setFieldValue(field, entity,
                            entityResultSet.getObject(ReflectionUtils.getForeignKey(field)));
                }
            }
        } catch (Exception e) {
            //Todo: logging an exception
            e.printStackTrace();
        }
        return entity;
    }
}

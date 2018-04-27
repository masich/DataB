package entities.base;

import entities.base.annotations.Field;
import entities.base.annotations.PrimaryKey;
import entities.base.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

abstract public class Entity {
    public final boolean save() {
        return save(this);
    }

    /**
     * Deletes an instance from appropriate database table.
     * <p>
     * Deletes an instance with the same primary key from appropriate
     * database table.
     *
     * @return <code>true</code> if entity was successfully deleted, otherwise <code>false</code>.
     */
    //Todo: refactor me
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
    //Todo: extract boilerplate code into external methods
    public static <T> List<T> getAll(final Class<T> entityClass) {
        try {
            String tableName = ReflectionUtils.getTableName(entityClass);
            List<java.lang.reflect.Field> entityFields = ReflectionUtils.getAllFields(entityClass);
            ResultSet entityResultSet = DBManager.getInstance().getConnection().createStatement()
                    .executeQuery("SELECT  *  FROM " + tableName);
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
            return DBManager.getInstance().getConnection().createStatement()
                    .executeUpdate("DELETE FROM " + ReflectionUtils.getTableName(entityClass)) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Todo: refactor me after creating an QueryBuilder
    public static boolean save(final Entity obj) {
        try {
            Class<? extends Entity> entityClass = obj.getClass();
            String tableName = ReflectionUtils.getTableName(entityClass);
            List<java.lang.reflect.Field> entityFields = ReflectionUtils.getAllFields(entityClass);
            StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");

            java.lang.reflect.Field primary = null;
            String id = null;

            for (java.lang.reflect.Field field : entityFields) {
                field.setAccessible(true);
                for (Annotation annotation : field.getDeclaredAnnotations()) {
                    if (annotation instanceof Field)
                        query.append(tableName + "." + ((Field) annotation).value() + " = '" + field.get(obj).toString() + "', ");
                    if (annotation instanceof PrimaryKey) {
                        primary = field;
                        id = ((PrimaryKey) annotation).value();
                    }
                }
            }
            query.deleteCharAt(query.lastIndexOf(","))
                    .append(" WHERE " + tableName + "." + id + " = '" + primary.get(obj) + "'");

            return DBManager.getInstance()
                    .getConnection()
                    .createStatement()
                    .executeUpdate(query.toString()) > 0;
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Todo: refactor me
    public static <T> T getById(Object id, Class<T> entityClass) {
        try {
            String tableName = ReflectionUtils.getTableName(entityClass);
            java.lang.reflect.Field primaryKeyField = ReflectionUtils.getPrimaryKeyField(entityClass);
            if (primaryKeyField == null) return null;
            String primaryKey = ReflectionUtils.getPrimaryKey(primaryKeyField);

            ResultSet entityResultSet = DBManager.getInstance().getConnection().createStatement()
                    .executeQuery("SELECT  *  FROM " + tableName
                            + " WHERE " + tableName + "." + primaryKey + " = " + id.toString());
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
            Object primaryKeyValue = ReflectionUtils.getFieldValue(primaryKeyField, obj);

            return DBManager.getInstance().getConnection().createStatement()
                    .executeUpdate("DELETE  FROM " + tableName
                            + " WHERE " + tableName + "." + primaryKey + " = " + primaryKeyValue) > 0;
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
                            entityResultSet.getObject(ReflectionUtils.getFieldName(field)));
                } else if (ReflectionUtils.isField(field)) {
                    ReflectionUtils.setFieldValue(field, entity,
                            entityResultSet.getObject(ReflectionUtils.getFieldName(field)));
                } else if (ReflectionUtils.isForeignKey(field)) {
                    ReflectionUtils.setFieldValue(field, entity,
                            entityResultSet.getObject(ReflectionUtils.getPrimaryKey(field)));
                }
            }
        } catch (Exception e) {
            //Todo: logging an exception
            e.printStackTrace();
        }
        return entity;
    }
}

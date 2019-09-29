package com.masich.datab.utils;

import com.masich.datab.DatabaseManager;
import com.masich.datab.Entity;
import com.masich.datab.converter.Converter;
import com.masich.datab.exception.FieldNotFoundException;
import com.masich.datab.query.Query;
import com.masich.datab.query.SQLQuery;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class DatabaseUtils {
    /**
     * Saves an entity to appropriate database table.
     * <p>
     * Saves an instance with corresponding primary key to appropriate
     * database table.
     *
     * @param saveRecursively if <code>true</code> inner entities will also be saved.
     * @param entity          entity to save.
     */
    //Todo: add exception logging
    public static void save(boolean saveRecursively, final Entity entity) {
        try {
            DatabaseManager databaseManager = DatabaseManager.getSingleton();
            databaseManager.beginTransaction();
            save(saveRecursively, entity, new HashSet<Entity>());
            databaseManager.finishTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FieldNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves (recursively by default) an iterable data structure of entities to the appropriate database table(s).
     * <p>
     * Saves the instances with corresponding primary key to the appropriate
     * database table(s). Simply calls {@link #saveAll(boolean, Iterable)} with a <code>true</code>
     * value for a boolean saveRecursively.
     *
     * @param entities entities to save.
     */
    public static void saveAll(final Iterable<? extends Entity> entities) {
        saveAll(true, entities);
    }

    /**
     * Saves an iterable data structure of entities to the appropriate database table(s).
     * <p>
     * Saves the instances with corresponding primary key to the appropriate
     * database table(s).
     *
     * @param saveRecursively if <code>true</code> inner entities will also be saved.
     * @param entities        entities to save.
     */
    public static void saveAll(boolean saveRecursively, final Iterable<? extends Entity> entities) {
        try {
            DatabaseManager databaseManager = DatabaseManager.getSingleton();
            databaseManager.beginTransaction();
            saveAll(saveRecursively, entities, new HashSet<Entity>());
            databaseManager.finishTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FieldNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes an entity from appropriate database table.
     * <p>
     * Deletes an instance with the same primary key from appropriate
     * database table.
     *
     * @param deleteRecursively if <code>true</code> inner entities will also be deleted.
     * @param obj               entity to delete.
     */
    //Todo: add exception logging
    public static void delete(boolean deleteRecursively, final Entity obj) {
        try {
            DatabaseManager databaseManager = DatabaseManager.getSingleton();
            databaseManager.beginTransaction();
            delete(deleteRecursively, obj, new HashSet<Entity>());
            databaseManager.finishTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FieldNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes all of the instances from the corresponding database table.
     *
     * @param entityClass class of the database table entity.
     */
    public static boolean deleteAll(final Class<? extends Entity> entityClass) {
        try {
            Query query = DatabaseManager.getSingleton().getSQLQueryBuilder()
                    .delete()
                    .from(ReflectionUtils.getTableName(entityClass))
                    .build();
            return DatabaseManager.getSingleton().executeUpdate(query) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns the <code>entityClass</code> entity from the
     * appropriate database table with the corresponding id.
     * Entity also will be recursively initialized.
     *
     * @param entityClass class of the database table entity.
     * @param <T>         type of the result value. The type of <code>entityClass</code> in this case.
     * @return the entity of the appropriate database table,
     * <code>null</code> if something went wrong.
     */
    public static <T> T getById(Object id, Class<T> entityClass) {
        return getById(id, entityClass, new HashMap<Class, Map<Object, Object>>());
    }

    /**
     * Returns all of the <code>entityClass</code> com from the
     * appropriate database table. Entities also will be recursively initialized.
     *
     * @param entityClass class of the database table entity.
     * @param <T>         type of the result value. The type of <code>entityClass</code> in this case.
     * @return all of the com of the appropriate database table,
     * emptyList if the database table is empty,
     * <code>null</code> if something went wrong.
     */
    public static <T> List<T> getAll(final Class<T> entityClass) {
        try {
            String tableName = ReflectionUtils.getTableName(entityClass);
            Query query = DatabaseManager.getSingleton().getSQLQueryBuilder().select().all().from(tableName).build();
            ResultSet entityResultSet = DatabaseManager.getSingleton().executeQuery(query);
            List<T> allEntities = new ArrayList<>();
            while (entityResultSet.next()) {
                T entity = ReflectionUtils.getNewInstance(entityClass);
                allEntities.add(initEntity(entity, entityResultSet));
            }
            return allEntities;
        } catch (Exception e) {
            //Todo: logging an exception
            e.printStackTrace();
        }
        return null;
    }

    //Todo: create entity deleting in exception case
    private static void save(boolean saveRecursively, final Entity obj, Set<Entity> saved) throws SQLException, FieldNotFoundException {
        Query query;
        if (!saved.contains(obj)) {
            Class<? extends Entity> entityClass = obj.getClass();
            if (Entity.class.isAssignableFrom(entityClass)) {
                saved.add(obj);
                String tableName = ReflectionUtils.getTableName(entityClass);
                List<Field> entityFields = ReflectionUtils.getAllFields(entityClass);
                SQLQuery.Builder queryBuilder = DatabaseManager.getSingleton().getSQLQueryBuilder()
                        .replace()
                        .into(tableName)
                        .values();
                SQLQuery.Chain.Builder valuesBuilder = DatabaseManager.getSingleton().getSQLQueryChainBuilder();
                for (Field field : entityFields) {
                    if (ReflectionUtils.isField(field)) {
                        Object fieldValue = ReflectionUtils.getFieldValue(field, obj);
                        valuesBuilder.appendUnit(DatabaseManager.getSingleton()
                                .getConverter()
                                .convertToString(fieldValue));
                    } else if (ReflectionUtils.isForeignKey(field)) {
                        Entity foreignKeyValue = (Entity) ReflectionUtils.getFieldValue(field, obj);
                        if (saveRecursively && foreignKeyValue != null) save(true, foreignKeyValue, saved);
                        valuesBuilder.appendUnit(ReflectionUtils.getPrimaryKeyValue(foreignKeyValue));
                    } else if (ReflectionUtils.isPrimaryKey(field)) {
                        valuesBuilder.appendUnit(ReflectionUtils.getFieldValue(field, obj));
                    }
                }
                query = queryBuilder.appendQueryPart(valuesBuilder.build()).build();
            } else {
                //Todo: Serialize another objects
                query = SQLQuery.EMPTY_QUERY;
            }
            DatabaseManager.getSingleton().executeUpdate(query);
        }
    }

    private static void saveAll(boolean saveRecursively, final Iterable<? extends Entity> entities, Set<Entity> saved) throws SQLException, FieldNotFoundException {
        for (Entity entity : entities) {
            save(saveRecursively, entity, saved);
        }
    }

    //Fixme: npe
    private static void delete(boolean deleteRecursively, final Entity obj, Set<Entity> deleted) throws FieldNotFoundException, SQLException {
        Class<? extends Entity> entityClass = obj.getClass();
        Query query;
        if (!deleted.contains(obj)) {
            deleted.add(obj);
            String tableName = ReflectionUtils.getTableName(entityClass);
            String primaryKey = null;
            Object id = null;
            List<Field> fields = ReflectionUtils.getAllFields(entityClass);
            for (Field field : fields) {
                if (ReflectionUtils.isForeignKey(field)) {
                    if (deleteRecursively) {
                        Entity foreignKeyValue = (Entity) ReflectionUtils.getFieldValue(field, obj);
                        delete(true, foreignKeyValue, deleted);
                    }
                } else if (ReflectionUtils.isPrimaryKey(field)) {
                    primaryKey = ReflectionUtils.getPrimaryKeyColumnName(field);
                    id = ReflectionUtils.getFieldValue(field, obj);
                }
            }
            if (primaryKey == null)
                throw new FieldNotFoundException("Class " + entityClass.getName() + " has no primary key!");
            SQLQuery.Builder queryBuilder = DatabaseManager.getSingleton().getSQLQueryBuilder()
                    .delete()
                    .from(tableName)
                    .where();
            SQLQuery.Condition condition = DatabaseManager.getSingleton().getSQLQueryConditionBuilder()
                    .equals(primaryKey, id)
                    .build();
            query = queryBuilder.appendQueryPart(condition).build();
            DatabaseManager.getSingleton().executeUpdate(query);
        }
    }

    private static <T> T getById(Object id, Class<T> entityClass, Map<Class, Map<Object, Object>> initialized) {
        try {
            if (!isInitialized(id, entityClass, initialized)) {
                String tableName = ReflectionUtils.getTableName(entityClass);
                Field primaryKeyField = ReflectionUtils.getPrimaryKeyField(entityClass);
                String primaryKey = ReflectionUtils.getPrimaryKeyColumnName(primaryKeyField);
                SQLQuery.Builder queryBuilder = DatabaseManager.getSingleton().getSQLQueryBuilder()
                        .select()
                        .all()
                        .from(tableName)
                        .where();
                SQLQuery.Condition condition = DatabaseManager.getSingleton().getSQLQueryConditionBuilder()
                        .equals(primaryKey, id)
                        .build();
                Query query = queryBuilder.appendQueryPart(condition).build();
                ResultSet entityResultSet = DatabaseManager.getSingleton().executeQuery(query);
                if (!entityResultSet.next()) return null;
                T entity = ReflectionUtils.getNewInstance(entityClass);
                addInitialized(entity, id, initialized);
                initEntity(entity, entityResultSet, initialized);
                return entity;
            } else {
                return getInitialized(id, entityClass, initialized);
            }
        } catch (Exception e) {
            //Todo: logging an exception
            e.printStackTrace();
            return null;
        }
    }

    private static <T> boolean isInitialized(Object id, Class<T> entityClass, Map<Class, Map<Object, Object>> initialized) {
        if (initialized.containsKey(entityClass)) {
            return initialized.get(entityClass).containsKey(id);
        } else {
            initialized.put(entityClass, new HashMap<Object, Object>());
            return false;
        }
    }

    private static <T> void addInitialized(T entity, Object id, Map<Class, Map<Object, Object>> initialized) {
        Class<?> entityClass = entity.getClass();
        if (!isInitialized(id, entityClass, initialized)) {
            initialized.get(entityClass).put(id, entity);
        }
    }

    //Fixme: unchecked cast
    private static <T> T getInitialized(Object id, Class<T> entityClass, Map<Class, Map<Object, Object>> initialized) {
        return (T) initialized.get(entityClass).get(id);
    }

    private static <T> T initEntity(T entity, ResultSet entityResultSet) throws SQLException {
        return initEntity(entity, entityResultSet, new HashMap<Class, Map<Object, Object>>());
    }

    private static <T> T initEntity(T entity, ResultSet entityResultSet, Map<Class, Map<Object, Object>> initialized) throws SQLException {
        Class<?> entityClass = entity.getClass();
        Converter converter = DatabaseManager.getSingleton()
                .getConverter();
        List<Field> entityFields = ReflectionUtils.getAllFields(entityClass);
        for (Field field : entityFields) {
            if (ReflectionUtils.isPrimaryKey(field)) {
                ReflectionUtils.setFieldValue(field, entity,
                        entityResultSet.getObject(ReflectionUtils.getPrimaryKeyColumnName(field)));
            } else if (ReflectionUtils.isField(field)) {
                String fieldName = ReflectionUtils.getFieldColumnName(field);
                Type fieldType = ReflectionUtils.getFieldType(field);
                Object result = entityResultSet.getObject(fieldName);
                if (result instanceof String) {
                    result = converter.convertFromString((String) result, fieldType);
                }
                ReflectionUtils.setFieldValue(field, entity, result);
            } else if (ReflectionUtils.isForeignKey(field)) {
                Object foreignKeyId = entityResultSet.getObject(ReflectionUtils.getForeignKeyColumnName(field));
                Class<?> foreignKeyClass = ReflectionUtils.getFieldClass(field);
                Object foreignKeyObject = getById(foreignKeyId, foreignKeyClass, initialized);
                ReflectionUtils.setFieldValue(field, entity, foreignKeyObject);
            }
        }
        return entity;
    }

    private DatabaseUtils() {
    }
}

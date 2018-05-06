package entities.base;

import entities.base.converters.base.Converter;
import entities.base.exceptions.FieldNotFoundException;
import entities.base.queries.MySQLQuery;
import entities.base.queries.base.SQLQuery;
import entities.base.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

abstract public class Entity {
    /**
     * Uses recursively saving by default.
     */
    public final void save() {
        save(true);
    }

    /**
     * @param saveRecursively if <code>true</code> the recursive saving will be used.
     */
    public final void save(boolean saveRecursively) {
        save(saveRecursively, this);
    }

    /**
     * Does not use recursively deletion by default.
     */
    public final void delete() {
        delete(false);
    }

    /**
     * @param deleteRecursively if <code>true</code> the recursive deletion will be used.
     */
    //Todo: make foreign key NULLABLE in DB as requirement (?)
    //Fixme: Is recursion delete necessary? You can achieve it using pure DB settings (simply make Constraint on Foreign Key 'CASCADE' on delete)
    private void delete(boolean deleteRecursively) {
        delete(deleteRecursively, this);
    }

    /**
     * Saves an entity to appropriate database table.
     * <p>
     * Saves an instance with corresponding primary key to appropriate
     * database table.
     *
     * @param saveRecursively if <code>true</code> inner entities will also be saved.
     * @param obj             entity to save.
     */
    //Todo: add exception logging
    public static void save(boolean saveRecursively, final Entity obj) {
        try {
            DBManager dbManager = DBManager.getSingleton();
            dbManager.beginTransaction();
            dbManager.checkForeignKey(false);
            save(saveRecursively, obj, new HashSet<>());
            dbManager.checkForeignKey(true);
            dbManager.finishTransaction();
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
            DBManager dbManager = DBManager.getSingleton();
            dbManager.beginTransaction();
            delete(deleteRecursively, obj, new HashSet<>());
            dbManager.finishTransaction();
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
            SQLQuery query = new MySQLQuery.Builder()
                    .delete()
                    .from(ReflectionUtils.getTableName(entityClass))
                    .build();
            return executeUpdate(query) > 0;
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
     * Returns all of the <code>entityClass</code> entities from the
     * appropriate database table. Entities also will be recursively initialized.
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
            ResultSet entityResultSet = executeQuery(query);
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
        Class<? extends Entity> entityClass = obj.getClass();
        SQLQuery query;
        if (!saved.contains(obj)) {
            if (Entity.class.isAssignableFrom(entityClass)) {
                saved.add(obj);
                String tableName = ReflectionUtils.getTableName(entityClass);
                List<Field> entityFields = ReflectionUtils.getAllFields(entityClass);
                MySQLQuery.Builder queryBuilder = new MySQLQuery.Builder()
                        .replace()
                        .into()
                        .appendQuery(tableName)
                        .values();
                MySQLQuery.Chain.Builder valuesBuilder = new MySQLQuery.Chain.Builder();
                for (Field field : entityFields) {
                    if (ReflectionUtils.isField(field)) {
                        Object fieldValue = ReflectionUtils.getFieldValue(field, obj);
                        valuesBuilder.appendUnit(DBManager.getSingleton().getConverter().convertToString(fieldValue));
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
                query = new MySQLQuery("");
            }
            executeUpdate(query);
        }
    }

    //Fixme: npe
    private static void delete(boolean deleteRecursively, final Entity obj, Set<Entity> deleted) throws FieldNotFoundException, SQLException {
        Class<? extends Entity> entityClass = obj.getClass();
        SQLQuery query;
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
                    primaryKey = ReflectionUtils.getPrimaryKey(field);
                    id = ReflectionUtils.getFieldValue(field, obj);
                }
            }
            if (primaryKey == null)
                throw new FieldNotFoundException("Class " + entityClass.getName() + " has no primary key!");
            MySQLQuery.Builder queryBuilder = new MySQLQuery.Builder()
                    .delete()
                    .from(tableName)
                    .where();
            MySQLQuery.Condition condition = new MySQLQuery.Condition.Builder()
                    .equals(primaryKey, id)
                    .build();
            query = queryBuilder.appendQueryPart(condition).build();
            executeUpdate(query);
        }
    }

    private static <T> T getById(Object id, Class<T> entityClass, Map<Class, Map<Object, Object>> initialized) {
        try {
            if (!isInitialized(id, entityClass, initialized)) {
                String tableName = ReflectionUtils.getTableName(entityClass);
                Field primaryKeyField = ReflectionUtils.getPrimaryKeyField(entityClass);
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
                ResultSet entityResultSet = executeQuery(query);
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
        Converter converter = DBManager.getSingleton().getConverter();
        List<Field> entityFields = ReflectionUtils.getAllFields(entityClass);
        for (Field field : entityFields) {
            if (ReflectionUtils.isPrimaryKey(field)) {
                ReflectionUtils.setFieldValue(field, entity,
                        entityResultSet.getObject(ReflectionUtils.getPrimaryKey(field)));
            } else if (ReflectionUtils.isField(field)) {
                String fieldName = ReflectionUtils.getFieldName(field);
                Class<?> fieldType = ReflectionUtils.getFieldType(field);
                Object result = entityResultSet.getObject(fieldName);
                if (result instanceof String) {
                    result = converter.convertFromString((String) result, fieldType);
                }
                ReflectionUtils.setFieldValue(field, entity, result);
            } else if (ReflectionUtils.isForeignKey(field)) {
                Object foreignKeyId = entityResultSet.getObject(ReflectionUtils.getForeignKey(field));
                Class<?> foreignKeyClass = ReflectionUtils.getFieldType(field);
                Object foreignKeyObject = getById(foreignKeyId, foreignKeyClass, initialized);
                ReflectionUtils.setFieldValue(field, entity, foreignKeyObject);
            }
        }
        return entity;
    }

    public static ResultSet executeQuery(String query) throws SQLException {
        return DBManager.getSingleton().getConnection().createStatement().executeQuery(query);
    }

    public static ResultSet executeQuery(SQLQuery query) throws SQLException {
        return executeQuery(query.toRawString());
    }

    public static Integer executeUpdate(String query) throws SQLException {
        return DBManager.getSingleton().getConnection().createStatement().executeUpdate(query);
    }

    public static Integer executeUpdate(SQLQuery query) throws SQLException {
        return executeUpdate(query.toRawString());
    }
}

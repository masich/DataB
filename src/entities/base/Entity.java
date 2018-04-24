package entities.base;

import com.sun.istack.internal.NotNull;
import entities.base.annotations.Field;
import entities.base.annotations.ForeignKey;
import entities.base.annotations.PrimaryKey;
import entities.base.annotations.Table;
//import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

abstract public class Entity<T extends Entity> {
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
    public static <T> List<T> getAll(@NotNull Class<T> entityClass) {
        try {
            String tableName = entityClass.getAnnotation(Table.class).value();
            java.lang.reflect.Field[] entityFields = entityClass.getDeclaredFields();
            ResultSet entityResultSet;
            entityResultSet = DBManager.getInstance().getConnection().createStatement()
                    .executeQuery("SELECT  *  FROM " + tableName);
            List<T> allEntities = new ArrayList<>();
            while (entityResultSet.next()) {
                T entity = entityClass.getDeclaredConstructor().newInstance();
                for (java.lang.reflect.Field field : entityFields) {
                    field.setAccessible(true);
                    for (Annotation annotation : field.getDeclaredAnnotations()) {
                        if (annotation instanceof PrimaryKey) {
                            field.set(entity, entityResultSet.getObject(((PrimaryKey) annotation).value()));
                        } else if (annotation instanceof ForeignKey) {
                            field.set(entity, entityResultSet.getObject(((ForeignKey) annotation).value()));
                        } else if (annotation instanceof Field) {
                            field.set(entity, entityResultSet.getObject(((Field) annotation).value()));
                        }
                    }
                }
                allEntities.add(entity);
            }
            return allEntities;
        } catch (Exception e) {
            //Todo: logging an exception
//            e.printStackTrace();
            return null;
        }
    }

    public static <T> boolean deleteAll(@NotNull Class<T> entityClass) {
        boolean result = false;
        try {
            String tableName = entityClass.getAnnotation(Table.class).value();
            DBManager.getInstance().getConnection().createStatement()
                    .execute("DELETE FROM " + tableName);
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <T> boolean update(T obj) throws IllegalAccessException {
        boolean result = false;

        Class<T> entityClass = (Class<T>) obj.getClass();
        String tableName = entityClass.getAnnotation(Table.class).value();

        java.lang.reflect.Field[] entityFields = entityClass.getDeclaredFields();

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


        try {
            result = DBManager.getInstance()
                    .getConnection()
                    .createStatement()
                    .execute(query.toString());
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    //Todo: refactor me
    public static <T> T getById(@NotNull Object id, @NotNull Class<T> entityClass) {
        try {
            String tableName = entityClass.getAnnotation(Table.class).value();
            String primaryKey = null;

            java.lang.reflect.Field[] entityFields = entityClass.getDeclaredFields();
            for (java.lang.reflect.Field field : entityFields) {
                PrimaryKey primaryKeyAnnotation = field.getAnnotation(PrimaryKey.class);
                if (primaryKeyAnnotation != null) {
                    primaryKey = primaryKeyAnnotation.value();
                    break;
                }
            }
            if (primaryKey == null) {
                return null;
            }
            ResultSet entityResultSet;
            entityResultSet = DBManager.getInstance().getConnection().createStatement()
                    .executeQuery("SELECT  *  FROM " + tableName
                            + " WHERE " + tableName + "." + primaryKey + " = " + id.toString());
            if (!entityResultSet.next()) {
                return null;
            }
            T entity = entityClass.getDeclaredConstructor().newInstance();
            for (java.lang.reflect.Field field : entityFields) {
                field.setAccessible(true);
                for (Annotation annotation : field.getDeclaredAnnotations()) {
                    if (annotation instanceof PrimaryKey) {
                        field.set(entity, entityResultSet.getObject(((PrimaryKey) annotation).value()));
                    } else if (annotation instanceof ForeignKey) {
                        field.set(entity, entityResultSet.getObject(((ForeignKey) annotation).value()));
                    } else if (annotation instanceof Field) {
                        field.set(entity, entityResultSet.getObject(((Field) annotation).value()));
                    }
                }
            }
            return entity;
        } catch (Exception e) {
            //Todo: logging an exception
            e.printStackTrace();
            return null;
        }
    }

    public final List<T> getAll() {
        Class<T> entityClass = (Class<T>) this.getClass();
        return getAll(entityClass);
    }

    //Fixme: unchecked cast
    public final T getById(@NotNull Object id) {
        Class<T> entityClass = (Class<T>) this.getClass();
        return getById(id, entityClass);
    }

    //    public boolean save() {
//
//    }
//

    /**
     * Deletes an instance from appropriate database table.
     * <p>
     * Deletes an instance with the same primary key from appropriate
     * database table.
     *
     * @return <code>true</code> if entity was successfully deleted, otherwise <code>false</code>.
     */
    //Todo: refactor me
    public boolean delete() {
        try {
            Class<T> entityClass = (Class<T>) this.getClass();
            String tableName = entityClass.getAnnotation(Table.class).value();
            String primaryKey = null;
            Object primaryKeyValue = null;

            java.lang.reflect.Field[] entityFields = entityClass.getDeclaredFields();
            for (java.lang.reflect.Field field : entityFields) {
                PrimaryKey primaryKeyAnnotation = field.getAnnotation(PrimaryKey.class);
                if (primaryKeyAnnotation != null) {
                    field.setAccessible(true);
                    primaryKey = primaryKeyAnnotation.value();
                    primaryKeyValue = field.get(this);
                    break;
                }
            }
            if (primaryKey == null) {
                return false;
            }
            int deleted = DBManager.getInstance().getConnection().createStatement()
                    .executeUpdate("DELETE  FROM " + tableName
                            + " WHERE " + tableName + "." + primaryKey + " = " + primaryKeyValue);
            return deleted > 0;
        } catch (Exception e) {
            //Todo: logging an exception
            e.printStackTrace();
            return false;
        }
    }

//    public boolean update() {
//
//    }
}

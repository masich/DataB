package entities.base;

import entities.base.annotations.Field;
import entities.base.annotations.ForeignKey;
import entities.base.annotations.PrimaryKey;
import entities.base.annotations.Table;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.sql.ResultSet;

abstract public class Entity<T extends Entity> {
//    public List<T> getAll() {
//
//    }

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
//            e.printStackTrace();
            return null;
        }
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
//    public boolean delete() {
//
//    }
//
//    public boolean update() {
//
//    }
}

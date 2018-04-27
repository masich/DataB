package entities.base.utils;

import entities.base.Entity;
import entities.base.annotations.PrimaryKey;
import entities.base.annotations.Table;

import java.lang.reflect.Field;
import java.util.*;


public class ReflectionUtils {
    public static String getPrimaryKey(Class<? extends Entity> entityClass) {
        String primaryKey = null;
        List<Field> fields = getAllFields(new ArrayList<>(), entityClass);

        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                primaryKey = field.getAnnotation(PrimaryKey.class).value();
            }
        }

        return primaryKey;
    }

    public static List<Field> getAllFields(Class<? extends Entity> entityClass) {
        return getAllFields(new ArrayList<>(), entityClass);
    }

    private static List<Field> getAllFields(List<Field> fields, Class<?> entityClass) {
        Collections.addAll(fields, entityClass.getDeclaredFields());
        if (entityClass.getSuperclass() != null) {
            getAllFields(fields, entityClass.getSuperclass());
        }

        return fields;
    }

    public static String getTableName(Class<? extends Entity> entityClass) {
        return entityClass.getAnnotation(Table.class).value();
    }
}

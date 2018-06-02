package datab.utils;

import datab.annotation.Field;
import datab.annotation.ForeignKey;
import datab.annotation.PrimaryKey;
import datab.annotation.Table;
import datab.exception.ConstructorNotFoundException;
import datab.exception.FieldNotFoundException;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


public class ReflectionUtils {
    public static java.lang.reflect.Field getPrimaryKeyField(final Class<?> entityClass) throws FieldNotFoundException {
        List<java.lang.reflect.Field> fields = getAllFields(new ArrayList<java.lang.reflect.Field>(), entityClass);

        for (java.lang.reflect.Field field : fields)
            if (isPrimaryKey(field))
                return field;

        throw new FieldNotFoundException("Class " + entityClass.getName() + " has no primary key!");
    }

    public static boolean setFieldValue(final java.lang.reflect.Field field, final Object obj, final Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static <T> Set<Class<? extends T>> getAllSubclassesByPackage(String packageName, Class<T> superClass) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getSubTypesOf(superClass);
    }

    public static Object getFieldValue(final java.lang.reflect.Field field, final Object obj) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPrimaryKey(final java.lang.reflect.Field primaryKeyField) {
        return primaryKeyField.getAnnotation(PrimaryKey.class).value();
    }

    public static <T> Object getPrimaryKeyValue(T obj) throws FieldNotFoundException {
        if (obj == null) return null;
        java.lang.reflect.Field primaryKeyField = getPrimaryKeyField(obj.getClass());
        return getFieldValue(primaryKeyField, obj);
    }

    public static String getFieldName(final java.lang.reflect.Field field) {
        return field.getAnnotation(Field.class).value();
    }

    public static String getForeignKey(final java.lang.reflect.Field foreignKeyField) {
        return foreignKeyField.getAnnotation(ForeignKey.class).value();
    }

    public static String getTableName(final Class<?> entityClass) {
        return entityClass.getAnnotation(Table.class).value();
    }

    public static List<java.lang.reflect.Field> getAllFields(final Class<?> entityClass) {
        return getAllFields(new ArrayList<java.lang.reflect.Field>(), entityClass);
    }

    public static boolean isPrimaryKey(final java.lang.reflect.Field field) {
        return field.isAnnotationPresent(PrimaryKey.class);
    }

    public static boolean isField(final java.lang.reflect.Field field) {
        return field.isAnnotationPresent(Field.class);
    }

    public static boolean isForeignKey(final java.lang.reflect.Field field) {
        return field.isAnnotationPresent(ForeignKey.class);
    }

    public static <T> T getNewInstance(final Class<T> entityClass) {
        try {
            Constructor<T> constructor = entityClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            new ConstructorNotFoundException("Class " + entityClass.getSimpleName() +
                    " must have one constructor without parameters.", e).printStackTrace();
        }
        return null;
    }

    public static Type getFieldType(java.lang.reflect.Field field) {
        return field.getGenericType();
    }

    public static Class<?> getFieldClass(java.lang.reflect.Field field) {
        return field.getType();
    }

    private static List<java.lang.reflect.Field> getAllFields(List<java.lang.reflect.Field> fields, Class<?> entityClass) {
        Collections.addAll(fields, entityClass.getDeclaredFields());
        if (entityClass.getSuperclass() != null) {
            getAllFields(fields, entityClass.getSuperclass());
        }

        return fields;
    }
}

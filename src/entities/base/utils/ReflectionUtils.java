package entities.base.utils;

import entities.base.Entity;
import entities.base.annotations.PrimaryKey;
import entities.base.annotations.Table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;


public class ReflectionUtils {
    public static String getPrimaryKey(Class<? extends Entity>  className){
        String primaryKey = null;

        ArrayList<Field> fields = getAllFields(new ArrayList<>(),className);

        for(Field field : fields){
            if(field.isAnnotationPresent(PrimaryKey.class)){
                primaryKey = field.getAnnotation(PrimaryKey.class).value();
            }
        }
        return primaryKey;
    }

    private static ArrayList<Field> getAllFields(ArrayList<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    public static String getTableName(Class<? extends Entity> entity){
        return entity.getAnnotation(Table.class).value();
    }


}

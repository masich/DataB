package datab.provider.datatype;

import datab.utils.JavaDataTypes;
import datab.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static datab.utils.ReflectionUtils.isForeignKey;

public class SQLiteAttributesConverter implements SQLFieldAttributes.Converter {
    @Override
    public SQLFieldAttributes.DataType fromJavaClass(Class typeClass) {
        if (JavaDataTypes.getIntTypes().contains(typeClass)) {
            return SQLiteDataType.INTEGER;
        } else if (JavaDataTypes.getFloatTypes().contains(typeClass)) {
            return SQLiteDataType.REAL;
        } else {
            return SQLiteDataType.TEXT;
        }
    }

    @Override
    public List<SQLFieldAttributes.DataType> fromJavaClasses(Iterable<Class<?>> typeClasses) {
        List<SQLFieldAttributes.DataType> dataTypes = new ArrayList<>();
        for (Class typeClass : typeClasses) {
            dataTypes.add(fromJavaClass(typeClass));
        }
        return dataTypes;
    }

    @Override
    public SQLFieldAttributes fromClassField(Field field) {
        SQLFieldAttributes attributes = new SQLFieldAttributes();
        attributes.setSQLDataType(fromJavaClass(field.getType()));
        if (ReflectionUtils.isField(field)) {
            attributes.setSQLSense(SQLFieldAttributes.Sense.FIELD);
            attributes.setSQLName(ReflectionUtils.getFieldName(field));
        } else if (isForeignKey(field)) {
            attributes.setSQLSense(SQLFieldAttributes.Sense.FOREIGN_KEY);
            attributes.setSQLName(ReflectionUtils.getForeignKey(field));
        } else if (ReflectionUtils.isPrimaryKey(field)) {
            attributes.setSQLSense(SQLFieldAttributes.Sense.PRIMARY_KEY);
            attributes.setSQLName(ReflectionUtils.getPrimaryKey(field));
        }

        return attributes;

    }

    @Override
    public List<SQLFieldAttributes> fromClassesFields(Iterable<Field> fields) {
        List<SQLFieldAttributes> attributes = new ArrayList<>();
        for (Field field : fields) {
            attributes.add(fromClassField(field));
        }
        return attributes;
    }
}
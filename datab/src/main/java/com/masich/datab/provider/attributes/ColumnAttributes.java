package com.masich.datab.provider.attributes;

import com.masich.datab.exception.FieldNotFoundException;
import com.masich.datab.provider.datatype.InternalDataTypes;
import com.masich.datab.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ColumnAttributes {
    private String columnName;
    private int internalDataType;
    private FieldSQLSense sqlSense;
    private String description = "";

    public ColumnAttributes(String columnName, int internalDataType, FieldSQLSense sqlSense) {
        this.columnName = columnName;
        this.internalDataType = internalDataType;
        this.sqlSense = sqlSense;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String sqlName) {
        this.columnName = sqlName;
    }

    public int getInternalDataType() {
        return internalDataType;
    }

    public void setInternalDataType(int dataType) {
        this.internalDataType = dataType;
    }

    public FieldSQLSense getSQLSense() {
        return sqlSense;
    }

    public void setSense(FieldSQLSense sqlFieldSQLSense) {
        this.sqlSense = sqlFieldSQLSense;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public enum FieldSQLSense {
        PRIMARY_KEY,
        FIELD,
        FOREIGN_KEY
    }

    public static ColumnAttributes fromEntityField(Field field) {
        Class<?> typeClass = ReflectionUtils.getFieldClass(field);
        FieldSQLSense sense;
        String columnName;

        if (ReflectionUtils.isField(field)) {
            sense = FieldSQLSense.FIELD;
            columnName = ReflectionUtils.getFieldColumnName(field);
        } else if (ReflectionUtils.isForeignKey(field)) {
            sense = FieldSQLSense.FOREIGN_KEY;
            columnName = ReflectionUtils.getForeignKeyColumnName(field);
            try {
                typeClass = ReflectionUtils.getPrimaryKeyClass(typeClass);
            } catch (FieldNotFoundException e) {
                e.printStackTrace();
            }
        } else if (ReflectionUtils.isPrimaryKey(field)) {
            sense = FieldSQLSense.PRIMARY_KEY;
            columnName = ReflectionUtils.getPrimaryKeyColumnName(field);
        } else {
            return null;
        }
        return new ColumnAttributes(columnName, InternalDataTypes.fromClassType(typeClass), sense);
    }

    public static List<ColumnAttributes> fromEntityFields(Iterable<Field> fields) {
        List<ColumnAttributes> attributes = new ArrayList<>();
        for (Field field : fields) {
            attributes.add(fromEntityField(field));
        }
        return attributes;
    }
}

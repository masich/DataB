package com.masich.datab.provider.attributes;

import com.masich.datab.Entity;
import com.masich.datab.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TableAttributes {
    private String tableName;
    private List<ColumnAttributes> columnAttributes;

    public TableAttributes(String tableName, List<ColumnAttributes> columnAttributes) {
        this.tableName = tableName;
        this.columnAttributes = columnAttributes;
    }

    public TableAttributes(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ColumnAttributes> getColumnAttributes() {
        return columnAttributes;
    }

    public void setColumnAttributes(List<ColumnAttributes> columnAttributes) {
        this.columnAttributes = columnAttributes;
    }

    public static TableAttributes fromEntityClass(Class<? extends Entity> entityClass) {
        List<Field> entityFields = ReflectionUtils.getAllFields((entityClass));
        List<ColumnAttributes> columnAttributes = ColumnAttributes.fromEntityFields(entityFields);
        String tableName = ReflectionUtils.getTableName(entityClass);
        return new TableAttributes(tableName, columnAttributes);
    }

    public static List<TableAttributes> fromEntityClasses(Iterable<Class<? extends Entity>> entityClasses) {
        List<TableAttributes> tableAttributesList = new ArrayList<>();
        for (Class<? extends Entity> entityClass : entityClasses) {
            tableAttributesList.add(fromEntityClass(entityClass));
        }
        return tableAttributesList;
    }
}

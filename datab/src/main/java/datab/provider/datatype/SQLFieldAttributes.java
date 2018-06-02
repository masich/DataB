package datab.provider.datatype;

import java.lang.reflect.Field;
import java.util.List;

public class SQLFieldAttributes {
    private String sqlName;
    private DataType dataType;
    private Sense sqlSense;
    private String description = "";

    public String getSqlName() {
        return sqlName;
    }

    public void setSQLName(String sqlName) {
        this.sqlName = sqlName;
    }

    public DataType getSQLDataType() {
        return dataType;
    }

    public void setSQLDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Sense getSQLSense() {
        return sqlSense;
    }

    public void setSQLSense(Sense sqlSense) {
        this.sqlSense = sqlSense;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public enum Sense {
        PRIMARY_KEY,
        FIELD,
        FOREIGN_KEY
    }

    public interface DataType {
        String getSQLName();
    }

    interface Converter {
        DataType fromJavaClass(Class<?> typeClass);

        List<DataType> fromJavaClasses(Iterable<Class<?>> typeClasses);

        SQLFieldAttributes fromClassField(Field field);

        List<SQLFieldAttributes> fromClassesFields(Iterable<Field> fields);
    }
}

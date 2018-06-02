package datab.provider.datatype;

public enum SQLiteDataType implements SQLFieldAttributes.DataType {
    INTEGER("INTEGER"),
    TEXT("TEXT"),
    REAL("REAL"),
    NULL("NULL"),
    BLOB("BLOB");

    private String name;

    SQLiteDataType(String dataTypeName) {
        this.name = dataTypeName;
    }

    public String getSQLName() {
        return name;
    }
}

package datab.provider;

public enum DataType {
    INTEGER("INTEGER"),
    TEXT("TEXT"),
    REAL("REAL"),
    NULL("NULL"),
    BLOB("BLOB");

    private String name;

    DataType(String dataTypeName) {
        this.name = dataTypeName;
    }

    public String getName() {
        return name;
    }
}

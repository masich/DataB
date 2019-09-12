package com.masich.datab.provider.datatype;

public enum SQLiteDataType implements DataType {
    INTEGER(InternalDataTypes.INTEGER | InternalDataTypes.BIGINT | InternalDataTypes.DOUBLE | InternalDataTypes.SMALLINT | InternalDataTypes.TINYINT | InternalDataTypes.BIT),
    TEXT(InternalDataTypes.ALL_TYPES),
    REAL(InternalDataTypes.DOUBLE | InternalDataTypes.FLOAT | InternalDataTypes.REAL),
    NULL(InternalDataTypes.NULL),
    BLOB(InternalDataTypes.BLOB | InternalDataTypes.VARBINARY | InternalDataTypes.LONGVARBINARY);

    private final int internalDataType;

    SQLiteDataType(int internalDataType) {
        this.internalDataType = internalDataType;
    }

    @Override
    public String getSQLName() {
        return name();
    }

    @Override
    public int getInternalDataType() {
        return internalDataType;
    }

    @Override
    public boolean hasInternalDataType(int dataType) {
        return (internalDataType & dataType) != 0;
    }
}

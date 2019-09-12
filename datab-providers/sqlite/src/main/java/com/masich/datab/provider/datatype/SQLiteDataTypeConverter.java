package com.masich.datab.provider.datatype;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDataTypeConverter implements DataType.Converter {
    @Override
    public DataType fromInternalType(int internalType) {
        for (SQLiteDataType dataType : SQLiteDataType.values()) {
            if (dataType.hasInternalDataType(internalType) && dataType != SQLiteDataType.TEXT)
                return dataType;
        }
        return SQLiteDataType.TEXT;
    }

    @Override
    public List<DataType> fromInternalTypes(Iterable<Integer> internalTypes) {
        List<DataType> dataTypes = new ArrayList<>();
        for (int internalType : internalTypes) {
            dataTypes.add(fromInternalType(internalType));
        }
        return dataTypes;
    }
}
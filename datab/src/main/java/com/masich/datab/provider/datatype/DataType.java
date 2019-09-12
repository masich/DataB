package com.masich.datab.provider.datatype;

import java.util.List;

public interface DataType {
    String getSQLName();

    int getInternalDataType();

    boolean hasInternalDataType(int internalDataType);

    interface Converter {
        DataType fromInternalType(int internalType);

        List<DataType> fromInternalTypes(Iterable<Integer> typeClasses);
    }
}
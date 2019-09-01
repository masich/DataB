package com.masich.datab.converter;

import java.lang.reflect.Type;

public interface Converter {
    /**
     * Converts provided object to string that will be saved into database table.
     * It is necessary for objects, who's classes are not the <code>DataB Entities</code> or primitives.
     *
     * @param obj Object to convert
     * @return Conversion result
     */
    String convertToString(Object obj);

    /**
     * Converts a <code>String</code> to the object. The converted object will have a type of <code>type</code>.
     * This method is used to restore objects of classes which are not the <code>DataB Entities</code> or primitives
     * from string when you are getting these objects from the database table.
     *
     * @param rawData Object, converted to string
     * @param type    Type of the object to restore
     * @param <T>     Type of the object to restore
     * @return Object of the <code>type</code> type restored from the provided string
     */
    <T> T convertFromString(String rawData, Type type);

    interface Factory {
        Converter getConverter();
    }
}

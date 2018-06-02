package datab.converter;

import java.lang.reflect.Type;

public interface Converter {
    String convertToString(Object obj);

    <T> T convertFromString(String rawData, Type type);

    interface Factory {
        Converter getConverter();
    }
}

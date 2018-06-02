package datab.converter;

public interface Converter {
    String convertToString(Object obj);

    <T> T convertFromString(String rawData, Class<T> convertClass);

    interface Factory {
        Converter getConverter();
    }
}

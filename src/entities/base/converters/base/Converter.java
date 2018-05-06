package entities.base.converters.base;

public interface Converter {
    String convertToString(Object obj);

    <T> T convertFromString(String rawData, Class<T> convertClass);
}

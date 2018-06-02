package datab.converter.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datab.converter.Converter;

public class GsonConverter implements Converter {
    private Gson gson;

    public GsonConverter(Gson gson) {
        this.gson = gson;
    }

    public GsonConverter() {
        this(new GsonBuilder().serializeNulls().create());
    }

    @Override
    public String convertToString(Object obj) {
        if (obj instanceof String) {
            return obj.toString();
        }
        return gson.toJson(obj);
    }

    @Override
    public <T> T convertFromString(String rawData, Class<T> convertClass) {
        if (String.class.isAssignableFrom(convertClass))
            return (T) rawData;
        return gson.fromJson(rawData, convertClass);
    }
}

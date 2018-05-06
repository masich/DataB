package entities.base.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.base.converters.base.Converter;

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
        return gson.toJson(obj);
    }

    @Override
    public <T> T convertFromString(String rawData, Class<T> convertClass) {
        if (String.class.isAssignableFrom(convertClass)) {
            return (T) rawData;
        }
        return gson.fromJson(rawData, convertClass);
    }
}

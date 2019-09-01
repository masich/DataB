package com.masich.datab.converter.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.masich.datab.converter.Converter;

import java.lang.reflect.Type;

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
        return (obj instanceof String) ? obj.toString() : gson.toJson(obj);
    }

    @Override
    public <T> T convertFromString(String rawData, Type type) {
        TypeToken typeToken = TypeToken.get(type);

        if (String.class.isAssignableFrom(typeToken.getRawType()))
            return (T) rawData;
        return gson.fromJson(rawData, type);
    }
}

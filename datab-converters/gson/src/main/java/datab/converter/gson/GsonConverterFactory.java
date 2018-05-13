package datab.converter.gson;

import datab.converter.Converter;

public class GsonConverterFactory implements Converter.Factory {
    @Override
    public Converter getConverter() {
        return new GsonConverter();
    }
}

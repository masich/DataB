package datab.converter.gson;

import datab.converter.Converter;

public class GsonConverterFactory implements Converter.Factory {
    private Converter converter;

    @Override
    public Converter getConverter() {
        if (converter == null)
            converter = new GsonConverter();
        return converter;
    }
}

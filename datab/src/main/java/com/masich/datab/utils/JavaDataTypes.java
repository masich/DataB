package com.masich.datab.utils;

import java.util.Arrays;
import java.util.List;

public class JavaDataTypes {
    private JavaDataTypes() {
    }

    public static List<? extends Class<? extends Number>> getIntTypes() {
        return Arrays.asList(
                int.class,
                long.class,
                short.class,
                byte.class,
                Integer.class,
                Short.class,
                Byte.class,
                Long.class
        );
    }

    public static List<? extends Class<? extends Number>> getFloatTypes() {
        return Arrays.asList(
                float.class,
                double.class,
                Float.class,
                Double.class
        );
    }
}

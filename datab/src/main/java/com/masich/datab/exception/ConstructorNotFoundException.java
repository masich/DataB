package com.masich.datab.exception;

/**
 * Exception that is thrown if the class that extends DataB Entity doesn't have a public empty constructor.
 */
public class ConstructorNotFoundException extends NoSuchMethodException {
    public ConstructorNotFoundException() {
        super();
    }

    public ConstructorNotFoundException(String message) {
        super(message);
    }

    public ConstructorNotFoundException(String message, Exception cause) {
        this(message);
        initCause(cause);
    }
}

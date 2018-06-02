package datab.exception;

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

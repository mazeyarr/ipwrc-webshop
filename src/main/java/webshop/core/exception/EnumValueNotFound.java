package webshop.core.exception;

public class EnumValueNotFound extends Exception {
    public EnumValueNotFound() {
    }

    public EnumValueNotFound(String message) {
        super(message);
    }

    public EnumValueNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public EnumValueNotFound(Throwable cause) {
        super(cause);
    }

    public EnumValueNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

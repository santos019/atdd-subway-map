package subway.line.exception;

public class LineNotFoundException extends RuntimeException{

    public LineNotFoundException() {
        super();
    }

    public LineNotFoundException(String message) {
        super(message);
    }

    public LineNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LineNotFoundException(Throwable cause) {
        super(cause);
    }

    protected LineNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package subway.line.exception;

public class LineNotFoundException extends RuntimeException{

    public LineNotFoundException(String message) {
        super(message);
    }

    protected LineNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

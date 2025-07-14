package gift.common.exception;

public class NoAuthorizationHeaderException extends RuntimeException {
    public NoAuthorizationHeaderException(String message) {
        super(message);
    }
}

package gateway.proximity.gatewayserver.exceptions;

public class NoAuthorizationHeaderException extends RuntimeException {
    public NoAuthorizationHeaderException(String message) {
        super(message);
    }
}

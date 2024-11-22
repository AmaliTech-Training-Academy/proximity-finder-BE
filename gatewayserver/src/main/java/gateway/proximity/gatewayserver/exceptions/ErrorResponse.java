package gateway.proximity.gatewayserver.exceptions;

public record ErrorResponse(
        int status,
        String message
) {}

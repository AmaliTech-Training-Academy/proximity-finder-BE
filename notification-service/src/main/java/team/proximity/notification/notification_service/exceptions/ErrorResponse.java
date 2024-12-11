package team.proximity.notification.notification_service.exceptions;

public record ErrorResponse(

          Integer status,
          String error,
          String message,
          Long timestamp
) {}

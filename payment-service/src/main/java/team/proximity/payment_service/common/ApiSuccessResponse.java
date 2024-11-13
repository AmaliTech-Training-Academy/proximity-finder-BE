package team.proximity.payment_service.common;

public record ApiSuccessResponse(
        String message,
        Boolean success
){}

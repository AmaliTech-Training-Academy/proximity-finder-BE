package auth.proximity.authservice.dto;

public record UserUpdateRequest(

        String userName,
        String phoneNumber,
        String businessOwnerName,
        String businessAddress
){}

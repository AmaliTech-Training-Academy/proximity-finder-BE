package auth.proximity.authservice.dto;

public record UserInfoResponse(

        Long userId,
        String userName,
        String email,
        String mobileNumber,
        String businessOwnerName,
        String profileImage,
        String businessAddress


) {
}

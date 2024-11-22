package auth.proximity.authservice.dto.user;

import auth.proximity.authservice.entity.Role;

public record UserInfoResponse(

        Long userId,
        String userName,
        String email,
        String mobileNumber,
        String businessOwnerName,
        String profileImage,
        String businessAddress,
        Role role
) {
}

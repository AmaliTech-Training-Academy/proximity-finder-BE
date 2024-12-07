package auth.proximity.authservice.dto.user;

import auth.proximity.authservice.entity.Role;
import auth.proximity.authservice.enums.AccountStatus;

public record UserInfoResponse(

        Long userId,
        String userName,
        String email,
        String mobileNumber,
        String businessOwnerName,
        String profileImage,
        String placeName,
        double latitude,
        double longitude,
        Role role,
        AccountStatus status

) {
}

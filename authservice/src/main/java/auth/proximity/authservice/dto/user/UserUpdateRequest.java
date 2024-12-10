
package auth.proximity.authservice.dto.user;
public record UserUpdateRequest(
        String userName,
        String phoneNumber,
        String businessOwnerName,
        String businessAddress
){}
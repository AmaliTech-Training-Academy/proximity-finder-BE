
package auth.proximity.authservice.dto.user;
public record UserUpdateRequest(
        String userName,
        String mobileNumber,
        String businessOwnerName,
        String placeName,
        double latitude,
        double longitude
){}
package auth.proximity.authservice.dto.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class RefreshTokenResponse {
    private String jwtAccessToken;
    private String jwtRefreshToken;
}

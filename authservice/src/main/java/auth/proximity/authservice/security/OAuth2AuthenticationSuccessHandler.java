package auth.proximity.authservice.security;
import auth.proximity.authservice.security.jwt.JwtUtils;
import auth.proximity.authservice.services.security.UserDetailsImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${spring.app.oauth2FrontendUrl}")
    private String frontendUrl;
    private final JwtUtils jwtUtils;

    public OAuth2AuthenticationSuccessHandler(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();

        if (principal instanceof DefaultOAuth2User oauth2User) {
            Map<String, Object> attributes = oauth2User.getAttributes();

            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_SEEKER"));

            UserDetailsImpl userDetails = new UserDetailsImpl(
                    (String) attributes.get("name"),
                    (String) attributes.get("email"),
                    authorities  // Pass the authorities
            );
            String token = jwtUtils.generateAccessToken(userDetails);
            log.info("Generated token {}", token);

            response.sendRedirect(frontendUrl +"?token=" + token);
        } else {
            throw new ServletException("OAuth2 Authentication failed: unexpected principal type.");
        }

    }
}

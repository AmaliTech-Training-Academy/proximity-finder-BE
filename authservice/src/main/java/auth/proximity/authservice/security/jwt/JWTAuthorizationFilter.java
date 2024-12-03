package auth.proximity.authservice.security.jwt;

import auth.proximity.authservice.services.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import static auth.proximity.authservice.security.jwt.JwtConstants.AUTH_HEADER;


@Component
@RequiredArgsConstructor
@Slf4j
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Skip filtering for specific endpoints
            if (request.getServletPath().equals("/api/auth/public/refresh-token")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Extract token from the Authorization header
            String accessToken = jwtUtils.extractTokenFromHeaderIfExists(request.getHeader(AUTH_HEADER));
            if (accessToken != null && jwtUtils.validateJwtToken(accessToken)) {
                // Get username and load user details
                String username = jwtUtils.getUserNameFromJwtToken(accessToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Set authentication in the SecurityContext
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                logger.info("Authenticated user: {}", username);
            } else if (accessToken == null) {
                logger.warn("No JWT token found in request");
            } else {
                logger.warn("Invalid JWT token provided");
            }

            // Continue the filter chain
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Unable to authenticate user");
        }
    }
}


package team.proximity.request_management.request_management.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);
    private final JwtUtils jwtUtils;

    public AuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractTokenFromHeader(request);

        if (StringUtils.hasText(token)) {
            try {
                Claims claims = jwtUtils.validateAndParseToken(token);
                setAuthenticationContext(claims, request);
            } catch (io.jsonwebtoken.ExpiredJwtException ex) {
                LOGGER.error("JWT expired: {}", ex.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token has expired");
                return;
            } catch (Exception ex) {
                LOGGER.error("JWT validation failed: {}", ex.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private void setAuthenticationContext(Claims claims, HttpServletRequest request) {
        String email = claims.getSubject();
        String username = claims.get("username", String.class);
        List<String> roles = claims.get("role", List.class);

        if (!StringUtils.hasText(email) || !StringUtils.hasText(username)) {
            LOGGER.warn("Invalid JWT claims: missing email or username");
            return;
        }

        List<GrantedAuthority> authorities = roles == null ? Collections.emptyList() : roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        LOGGER.info("Authenticated user: email={}, username={}, roles={}", email, username, roles);

        AppUser appUser = new AppUser(username, email, authorities);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                appUser, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}



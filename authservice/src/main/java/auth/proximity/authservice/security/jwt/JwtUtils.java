package auth.proximity.authservice.security.jwt;

import auth.proximity.authservice.exception.TokenExpiredException;
import auth.proximity.authservice.services.security.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;

import static auth.proximity.authservice.security.jwt.JwtConstants.*;


@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtExpiration}")
    private  Long expireAccessToken;

    @Value("${spring.app.jwtRefreshExpiration}")
    private Long expirejwtRefreshToken;

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    public String generateAccessToken(UserDetailsImpl userDetails) {
        List<String> role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .subject(userDetails.getEmail())
                .claim("role", role)
                .claim("username", userDetails.getUsername())
                .claim("id", userDetails.getId())
                .issuer(ISSUER)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expireAccessToken))
                .signWith(key())
                .compact();
    }

    public String generateRefreshToken(UserDetailsImpl userDetails) {
        List<String> role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .issuer(ISSUER)
                .subject(userDetails.getEmail())
                .claim("role", role)
                .claim("username", userDetails.getUsername())
                .claim("id", userDetails.getId())
                .expiration(new Date(System.currentTimeMillis() + expirejwtRefreshToken))
                .signWith(key())
                .compact();
    }

    public String extractTokenFromHeaderIfExists(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());

            throw new TokenExpiredException("JWT token is expired");
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());

        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
package auth.proximity.authservice.services;

import auth.proximity.authservice.dto.ResponseDto;
import auth.proximity.authservice.dto.security.InfoResponse;
import auth.proximity.authservice.dto.security.LoginRequest;
import auth.proximity.authservice.dto.security.LoginResponse;
import auth.proximity.authservice.dto.security.RefreshTokenResponse;
import auth.proximity.authservice.entity.User;
import auth.proximity.authservice.security.jwt.JwtConstants;
import auth.proximity.authservice.security.jwt.JwtUtils;
import auth.proximity.authservice.services.security.UserDetailsImpl;
import auth.proximity.authservice.services.security.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final IUserService userService;

    public AuthService(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService, IUserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String jwtAccessToken = jwtUtils.generateAccessToken(userDetails);
            String jwtRefreshToken = jwtUtils.generateRefreshToken(userDetails);

            LoginResponse response = new LoginResponse(
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles,
                    jwtAccessToken,
                    jwtRefreshToken);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("401", "Invalid credentials"));
        }
    }
    public ResponseEntity<?> generateNewAccessToken(HttpServletRequest request) {
        String jwtRefreshToken = jwtUtils.extractTokenFromHeaderIfExists(request.getHeader(JwtConstants.AUTH_HEADER));
        if (jwtRefreshToken != null && jwtUtils.validateJwtToken(jwtRefreshToken)) {
            try {
                String email = jwtUtils.getUserNameFromJwtToken(jwtRefreshToken);
                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);
                String jwtAccessToken = jwtUtils.generateAccessToken(userDetails);
                RefreshTokenResponse tokenResponse = new RefreshTokenResponse(jwtRefreshToken, jwtAccessToken);
                return ResponseEntity.ok(tokenResponse);
            } catch (Exception e) {
                Map<String, Object> map = new HashMap<>();
                map.put("message", "Error processing refresh token");
                map.put("status", false);
                return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Invalid JWT Refresh token");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_ACCEPTABLE);
        }
    }
    public ResponseEntity<?> getUserDetails (UserDetailsImpl userDetails) {
        User user = userService.findByEmail(userDetails.getEmail());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        InfoResponse response = new InfoResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getBusinessOwnerName(),
                roles
        );

        return ResponseEntity.ok().body(response);
    }
}
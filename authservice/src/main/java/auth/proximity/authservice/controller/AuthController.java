package auth.proximity.authservice.controller;

import auth.proximity.authservice.entity.User;

import auth.proximity.authservice.security.dto.LoginRequest;
import auth.proximity.authservice.security.dto.LoginResponse;
import auth.proximity.authservice.security.dto.UserInfoResponse;
import auth.proximity.authservice.security.jwt.JwtConstants;
import auth.proximity.authservice.security.jwt.JwtUtils;
import auth.proximity.authservice.security.service.UserDetailsImpl;
import auth.proximity.authservice.security.service.UserDetailsServiceImpl;
import auth.proximity.authservice.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final IUserService userService;

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userService.findByEmail(userDetails.getEmail());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getBusinessOwnerName(),
                roles
        );

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/public/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        String jwtAccessToken = jwtUtils.generateAccessToken(userDetails.getEmail(), roles);
        String jwtRefreshToken = jwtUtils.generateRefreshToken(userDetails.getEmail());


        LoginResponse response = new LoginResponse(userDetails.getUsername(), userDetails.getEmail(), roles, jwtAccessToken, jwtRefreshToken);


        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/refresh-token")
    public void generateNewAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String jwtRefreshToken = jwtUtils.extractTokenFromHeaderIfExists(request.getHeader(JwtConstants.AUTH_HEADER));
        if(jwtRefreshToken != null &&  jwtUtils.validateJwtToken(jwtRefreshToken)) {
            String email = jwtUtils.getUserNameFromJwtToken(jwtRefreshToken);
            UserDetailsImpl user = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);
            String jwtAccessToken = jwtUtils.generateAccessToken(user.getEmail(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
            response.setContentType("application/json");
            new ObjectMapper().writeValue(response.getOutputStream(), jwtUtils.getTokensMap(jwtAccessToken, jwtRefreshToken));
        } else {
            throw new RuntimeException("Refresh token required");
        }
    }
}

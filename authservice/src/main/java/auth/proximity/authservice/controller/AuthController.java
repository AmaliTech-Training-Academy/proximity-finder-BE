package auth.proximity.authservice.controller;

import auth.proximity.authservice.dto.*;
import auth.proximity.authservice.entity.User;

import auth.proximity.authservice.security.dto.LoginRequest;
import auth.proximity.authservice.security.dto.LoginResponse;
import auth.proximity.authservice.security.dto.RefreshTokenResponse;
import auth.proximity.authservice.security.dto.InfoResponse;
import auth.proximity.authservice.security.jwt.JwtConstants;
import auth.proximity.authservice.security.jwt.JwtUtils;
import auth.proximity.authservice.security.service.UserDetailsImpl;
import auth.proximity.authservice.security.service.UserDetailsServiceImpl;
import auth.proximity.authservice.service.IUserService;
import auth.proximity.authservice.service.ProfilePictureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final IUserService userService;
    private final ProfilePictureService profilePictureService;

    @Operation(summary = "Get Current User REST API", description = "REST API to retrieve current with jwtAccessToken")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "406", description = "HTTP Status Not Acceptable", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetailsImpl userDetails) {
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


    @Operation(summary = "Sign-in REST API", description = "REST API to log users in which returns both jwtAccessToken and jwtRefreshToken")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "400", description = "HTTP Status Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/public/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String jwtAccessToken = jwtUtils.generateAccessToken(userDetails);
        String jwtRefreshToken = jwtUtils.generateRefreshToken(userDetails);

        LoginResponse response = new LoginResponse(userDetails.getUsername(), userDetails.getEmail(), roles, jwtAccessToken, jwtRefreshToken);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Create User REST API", description = "REST API to create new user inside Proximity Finder")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
            @ApiResponse(responseCode = "406", description = "HTTP Status Not Acceptable", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/public/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("201", "User created successfully"));
    }


    @Operation(summary = "Get AccessToken REST API", description = "REST API to retrieve a new jwtAccessToken with jwtRefreshToken without signing in")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "406", description = "HTTP Status Not Acceptable", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/public/refresh-token")
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


    @Operation(summary = "Validate Token REST        API", description = "REST API to validate a token to confirm whether valid or invalid")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/public/validate-token")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
        jwtUtils.validateJwtToken(token);
        return ResponseEntity.ok("Token is valid");
    }
    @PutMapping("/public/update-password")
    public ResponseEntity<ResponseDto> updatePassword(@RequestParam String email, @RequestBody AdminUpdatePasswordRequest adminUpdatePasswordRequest) {
        userService.updatePassword(email,adminUpdatePasswordRequest);
        return ResponseEntity.ok(new ResponseDto("200", "Password updated successfully"));
    }
    @PutMapping("/public/update-profile-picture")
    public ResponseEntity<String> uploadProfilePicture(@AuthenticationPrincipal UserDetailsImpl userDetails, @ModelAttribute ProfilePictureUpdateRequest profilePictureUpdateRequest) {
        if (userDetails == null) {
            return new ResponseEntity<>("Unauthorized: Token is missing or invalid", HttpStatus.UNAUTHORIZED);
        }
        String email = userDetails.getEmail();
        try {
            String fileUrl = profilePictureService.updateProfilePicture(email, profilePictureUpdateRequest);
            return new ResponseEntity<>(fileUrl, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload profile picture", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(@RequestParam String email) {
        UserInfoResponse userInfoResponse = userService.getUserInfo(email);
        return ResponseEntity.ok(userInfoResponse);
    }
    @PutMapping("/update/info")
    ResponseEntity<ResponseDto> updateUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserUpdateRequest userUpdateRequest) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("401", "Unauthorized: Token is missing or invalid"));
        }
        String email = userDetails.getEmail();
        userService.updateUserInfoByEmail(email, userUpdateRequest);
        return ResponseEntity.ok(new ResponseDto("200", "User updated successfully"));
    }

    @DeleteMapping("/profile-picture")
    public ResponseEntity<ResponseDto> deleteProfilePicture(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("401", "Unauthorized: Token is missing or invalid"));
        }
        String email = userDetails.getEmail();
        userService.deleteProfilePicture(email);
        return ResponseEntity.ok(new ResponseDto("200", "Profile picture deleted successfully"));
    }

}

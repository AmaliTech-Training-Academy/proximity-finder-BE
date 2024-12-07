package auth.proximity.authservice.controller;

import auth.proximity.authservice.dto.*;
import auth.proximity.authservice.dto.user.AdminUpdatePasswordRequest;
import auth.proximity.authservice.dto.user.UserDto;
import auth.proximity.authservice.dto.user.UserInfoResponse;
import auth.proximity.authservice.dto.user.UserUpdateRequest;
import auth.proximity.authservice.entity.User;

import auth.proximity.authservice.dto.security.LoginRequest;
import auth.proximity.authservice.dto.security.LoginResponse;
import auth.proximity.authservice.dto.security.RefreshTokenResponse;
import auth.proximity.authservice.dto.security.InfoResponse;
import auth.proximity.authservice.enums.AccountStatus;
import auth.proximity.authservice.security.jwt.JwtConstants;
import auth.proximity.authservice.security.jwt.JwtUtils;
import auth.proximity.authservice.services.security.UserDetailsImpl;
import auth.proximity.authservice.services.security.UserDetailsServiceImpl;
import auth.proximity.authservice.services.IUserService;
import auth.proximity.authservice.services.ProfilePictureService;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
        try {
            if(userService.findByEmail(loginRequest.getEmail()).getStatus().equals(AccountStatus.DEACTIVATED)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("401", "Account is inactive"));
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

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
        if(!jwtUtils.validateJwtToken(token)){
            return ResponseEntity.status(403).body("Token is not valid");
        }
        return ResponseEntity.ok("Token is valid");
    }
    @PutMapping("/public/update-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDto> updatePassword(
            @RequestParam String email,
            @RequestBody AdminUpdatePasswordRequest adminUpdatePasswordRequest) {

        userService.updatePassword(email, adminUpdatePasswordRequest);
        return ResponseEntity.ok(new ResponseDto("200", "Password updated successfully"));
    }

    @PutMapping("/public/update-profile-picture")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> uploadProfilePicture(@AuthenticationPrincipal UserDetailsImpl userDetails, @ModelAttribute ProfilePictureUpdateRequest profilePictureUpdateRequest) {
        try {
            String fileUrl = profilePictureService.updateProfilePicture(userDetails.getEmail(), profilePictureUpdateRequest);
            return new ResponseEntity<>(fileUrl, HttpStatus.OK);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile picture");
        }
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(@RequestParam String email) {
        UserInfoResponse userInfoResponse = userService.getUserInfo(email);
        return ResponseEntity.ok(userInfoResponse);
    }
    @PutMapping("/update/info")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDto> updateUserInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserUpdateRequest userUpdateRequest) {

        userService.updateUserInfoByEmail(userDetails.getEmail(), userUpdateRequest);
        return ResponseEntity.ok(new ResponseDto("200", "User updated successfully"));
    }

    @DeleteMapping("/profile-picture")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDto> deleteProfilePicture(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        String email = userDetails.getEmail();
        userService.deleteProfilePicture(email);
        return ResponseEntity.ok(new ResponseDto("200", "Profile picture deleted successfully"));
    }


}

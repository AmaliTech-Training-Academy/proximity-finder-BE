package auth.proximity.authservice.services;

import auth.proximity.authservice.dto.RejectionEmailRequest;
import auth.proximity.authservice.dto.user.AdminUpdatePasswordRequest;
import auth.proximity.authservice.dto.user.UserDto;
import auth.proximity.authservice.dto.user.UserInfoResponse;
import auth.proximity.authservice.dto.user.UserUpdateRequest;
import auth.proximity.authservice.entity.User;
import auth.proximity.authservice.enums.AccountStatus;

public interface IUserService {

    public UserInfoResponse getUserInfo(String email);

    /**
     *
     * @param email - - Input User email address
     * @return  User's Details based on a given email address
     * */
    User findByEmail(String email);

    /**
     *
     * @param userDto - UserDto Object
     * */
    void createUser(UserDto userDto);
    void changeUserStatus(Long userId, String status);
    void updatePassword(String email, AdminUpdatePasswordRequest adminUpdatePasswordRequest);
    void updateUserInfoByEmail(String email, UserUpdateRequest userUpdateRequest);
    void deleteProfilePicture(String email);
    void sendRejectionEmail(RejectionEmailRequest rejectionEmailRequest);
}

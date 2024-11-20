package auth.proximity.authservice.service;

import auth.proximity.authservice.dto.*;
import auth.proximity.authservice.entity.User;

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

    void updatePassword(String email, AdminUpdatePasswordRequest adminUpdatePasswordRequest);
    void updateUserInfoByEmail(String email, UserUpdateRequest userUpdateRequest);
}

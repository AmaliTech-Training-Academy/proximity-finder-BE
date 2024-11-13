package auth.proximity.authservice.service;

import auth.proximity.authservice.dto.*;
import auth.proximity.authservice.entity.User;

public interface IUserService {

    UserInfoResponse getUserInfo(String email);
    void updateUserInfoByEmail(String email, UserUpdateRequest userUpdateRequest);

    User findByEmail(String email);

    void createUser(UserDto userDto);

    void updatePassword(String email, AdminUpdatePasswordRequest adminUpdatePasswordRequest);
}

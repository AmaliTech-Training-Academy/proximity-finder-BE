package auth.proximity.authservice.service;

import auth.proximity.authservice.dto.AdminUpdatePasswordRequest;
import auth.proximity.authservice.dto.ProfilePictureUpdateRequest;
import auth.proximity.authservice.dto.UserDto;
import auth.proximity.authservice.entity.User;

public interface IUserService {

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
}

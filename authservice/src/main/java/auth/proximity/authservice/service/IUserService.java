package auth.proximity.authservice.service;

import auth.proximity.authservice.dto.NoteDto;
import auth.proximity.authservice.dto.UserDto;
import auth.proximity.authservice.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

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

}

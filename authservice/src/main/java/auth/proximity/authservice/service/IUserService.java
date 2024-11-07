package auth.proximity.authservice.service;

import auth.proximity.authservice.entity.User;

public interface IUserService {

    User findByEmail(String email);
}

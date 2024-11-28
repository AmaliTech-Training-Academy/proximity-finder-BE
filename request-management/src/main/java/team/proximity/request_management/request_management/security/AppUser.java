package team.proximity.request_management.request_management.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


import java.util.Collection;
@Getter
public class AppUser extends User {


    private final String email;

    public AppUser(String username, String email, Collection<? extends GrantedAuthority> authorities) {
        super(username, "", authorities);
        this.email = email;
    }
}


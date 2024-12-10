package auth.proximity.authservice.dto.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InfoResponse {

    private Long id;
    private String username;
    private String email;
    private String mobileNumber;
    private String businessOwnerName;
    private List<String> roles;

}
package auth.proximity.authservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String userName;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "mobileNumber")
    private String mobileNumber;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    private String businessOwnerName;

    private boolean accountNonLocked = true;

    private boolean accountNonExpired = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @JsonBackReference
    @ToString.Exclude
    private Role role;

    private String profileImage;

    private String businessAddress;


    public User(String userName, String email, String password, String mobileNumber) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.mobileNumber = mobileNumber;
    }
}

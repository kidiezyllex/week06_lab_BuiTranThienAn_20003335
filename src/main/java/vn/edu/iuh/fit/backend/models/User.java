package vn.edu.iuh.fit.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(15)")
    private String mobile;
    @Column(name = "lastLogin",columnDefinition = "DATETIME(6)")
    private Instant lastLogin;
    @Column(name = "lastName",columnDefinition = "VARCHAR(50)")
    private String lastName;
    @Column(columnDefinition = "TINYTEXT")
    private String intro;
    @Column(columnDefinition = "tinytext")
    private String profile;
    @Column(name = "registeredAt",columnDefinition = "DATETIME(6)", nullable = false)
    private Instant registeredAt;
    @Column(name = "passwordHash",columnDefinition = "VARCHAR(100)", nullable = false)
    private String passwordHash;
    @Column(name = "middleName", columnDefinition = "VARCHAR(50)")
    private String middleName;
    @Column(name = "firstName",columnDefinition = "VARCHAR(50)")
    private String firstName;
    @Column(columnDefinition = "VARCHAR(50)", unique = true)
    private String email;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Post> posts;

    public User(String mobile, Instant lastLogin, String lastName, String intro, String profile, Instant registeredAt, String passwordHash, String middleName, String firstName, String email) {
        this.mobile = mobile;
        this.lastLogin = lastLogin;
        this.lastName = lastName;
        this.intro = intro;
        this.profile = profile;
        this.registeredAt = registeredAt;
        this.passwordHash = passwordHash;
        this.middleName = middleName;
        this.firstName = firstName;
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", mobile='" + mobile + '\'' +
                ", lastLogin=" + lastLogin +
                ", lastName='" + lastName + '\'' +
                ", intro='" + intro + '\'' +
                ", profile='" + profile + '\'' +
                ", registeredAt=" + registeredAt +
                ", passwordHash='" + passwordHash + '\'' +
                ", middleName='" + middleName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

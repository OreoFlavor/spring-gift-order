package gift.user.domain;

import gift.OAuth.OAuthToken;
import gift.auth.PasswordUtil;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Base64;

@Entity
@Table(name = "\"user\""    )
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String salt;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private OAuthToken oAuthToken;

    protected User() {

    }

    public User(Long id, String email, String password, String salt, OAuthToken oAuthToken) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.oAuthToken = oAuthToken;
    }

    public User(Long id, String email, String password, String salt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.salt = salt;
    }

    public User(String email, String password, String salt, OAuthToken oAuthToken) {
        this(null, email, password, salt, oAuthToken);
    }

    public User(String email, String password, String salt) {
        this(null, email, password, salt, new OAuthToken());
    }
  
    public boolean isEqualToPassword(String password) {
        byte[] salt = Base64.getDecoder().decode(this.salt);
        String hashedPassword = PasswordUtil.encryptPassword(password, salt);
        return this.password.equals(hashedPassword);
    }

    public void changeEmail(String email) {
        if(email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
        this.email = email;
    }

    public void changePassword(String password) {
        if(password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public OAuthToken getOAuthToken() {
        return oAuthToken;
    }

    public void setOAuthToken(OAuthToken oAuthToken) {
        this.oAuthToken = oAuthToken;
        oAuthToken.setUser(this);
    }
}

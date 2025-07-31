package gift.OAuth;

import gift.user.domain.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class OAuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String accessToken;

    private String refreshToken;

    private Instant accessTokenExpiredAt;

    private Instant refreshTokenExpiredAt;

    public OAuthToken() {}

    public OAuthToken(String accessToken, String refreshToken, Instant accessTokenExpiredAt, Instant refreshTokenExpiredAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiredAt = accessTokenExpiredAt;
        this.refreshTokenExpiredAt = refreshTokenExpiredAt;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Instant getAccessTokenExpiredAt() {
        return accessTokenExpiredAt;
    }

    public Instant getRefreshTokenExpiredAt() {
        return refreshTokenExpiredAt;
    }

    public void setUser(User user) {
        this.user = user;
    }
}


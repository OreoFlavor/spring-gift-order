package gift.kakao;

import gift.user.domain.User;

import java.time.Instant;

public class KakaoUserPatchRequestDto {
    private String email;
    private String password;
    private String accessToken;
    private String refreshToken;
    private Instant accessTokenExpiredAt;
    private Instant refreshTokenExpiredAt;

    protected KakaoUserPatchRequestDto() {}

    public KakaoUserPatchRequestDto(String email, String password, String accessToken, String refreshToken, Instant accessTokenExpiredAt, Instant refreshTokenExpiredAt) {
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiredAt = accessTokenExpiredAt;
        this.refreshTokenExpiredAt = refreshTokenExpiredAt;
    }

    public KakaoUserPatchRequestDto(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

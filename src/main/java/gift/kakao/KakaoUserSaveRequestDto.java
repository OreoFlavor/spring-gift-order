package gift.kakao;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class KakaoUserSaveRequestDto {
    @NotNull
    private String email;
    @NotNull
    private String password;

    private String accessToken;

    private String refreshToken;

    private Instant accessTokenExpiredAt;

    private Instant refreshTokenExpiredAt;

    public KakaoUserSaveRequestDto() {};

    public KakaoUserSaveRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public KakaoUserSaveRequestDto(String email, String password, String accessToken, String refreshToken, Instant accessTokenExpiredAt, Instant refreshTokenExpiredAt) {
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiredAt = accessTokenExpiredAt;
        this.refreshTokenExpiredAt = refreshTokenExpiredAt;
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
}

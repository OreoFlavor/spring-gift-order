package gift.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoTokenResponseDto {
    public String tokenType;

    public String accessToken;

    public String idToken;

    public Integer expiresIn;

    public String refreshToken;

    public Integer refreshTokenExpiresIn;

    public String scope;

    protected KakaoTokenResponseDto() {}

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Integer getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }

    public String getScope() {
        return scope;
    }
}

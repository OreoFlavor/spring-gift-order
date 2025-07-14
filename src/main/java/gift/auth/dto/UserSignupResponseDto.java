package gift.auth.dto;

import gift.user.domain.User;

public class UserSignupResponseDto {
    private User user;
    private String token;

    public UserSignupResponseDto(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}

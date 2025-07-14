package gift.user.dto;

import gift.user.domain.User;

public class UserPatchRequestDto {
    String email;
    String password;

    protected UserPatchRequestDto() {}

    public UserPatchRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserPatchRequestDto(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

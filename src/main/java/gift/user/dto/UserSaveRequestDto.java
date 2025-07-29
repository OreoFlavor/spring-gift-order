package gift.user.dto;

import jakarta.validation.constraints.NotNull;

public class UserSaveRequestDto {
    @NotNull
    private String email;
    @NotNull
    private String password;

    public UserSaveRequestDto() {};

    public UserSaveRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
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

package gift.user.dto;

import jakarta.validation.constraints.NotNull;

public class UserSaveRequestDto {
    @NotNull
    private final String email;
    @NotNull
    private final String password;

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
}

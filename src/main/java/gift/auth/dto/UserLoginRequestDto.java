package gift.auth.dto;

import jakarta.validation.constraints.NotNull;

public class UserLoginRequestDto {
    @NotNull
    private String email;
    @NotNull
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

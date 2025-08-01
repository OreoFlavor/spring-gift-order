package gift.user.dto;

public class UserPatchRequestDto {
    private final String email;
    private final String password;

    public UserPatchRequestDto(String email, String password) {
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

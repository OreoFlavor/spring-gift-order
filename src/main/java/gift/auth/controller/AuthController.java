package gift.auth.controller;

import gift.auth.dto.UserSignupResponseDto;
import gift.auth.dto.UserLoginRequestDto;
import gift.auth.service.AuthService;
import gift.auth.dto.UserSingupRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> singup(@Valid @RequestBody UserSingupRequestDto userSignupRequestDto) {
        UserSignupResponseDto userSignupResponseDto = authService.signUp(userSignupRequestDto);

        return ResponseEntity
                .created(URI.create("/api/auth/" + userSignupResponseDto.getUser().getId()))
                .body(userSignupResponseDto.getToken());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
        String token = authService.login(userLoginRequestDto);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token).body(token);
    }
}

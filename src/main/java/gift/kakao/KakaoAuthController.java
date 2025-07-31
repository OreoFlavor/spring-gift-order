package gift.kakao;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/kakaoAuth")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/login/page")
    public String loginPage(Model model) {
        String location = kakaoAuthService.getLoginUrl();
        model.addAttribute("location", location);
        return "kakaoLogin";
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        String token = kakaoAuthService.kakaoUserLogin(code);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token).body(token);
    }
}

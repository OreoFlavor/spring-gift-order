package gift.kakao;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String callback(@RequestParam("code") String code) {
        String userId = kakaoAuthService.getUserId(kakaoAuthService.getAccessToken(code));
        kakaoAuthService.kakaoUserLogin(userId);
        return "redirect:/api/admin/user/list";
    }
}

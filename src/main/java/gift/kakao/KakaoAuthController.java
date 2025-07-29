package gift.kakao;

import gift.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/kakaoAuth")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;
    private final UserService userService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService, UserService userService) {
        this.kakaoAuthService = kakaoAuthService;
        this.userService = userService;
    }

    @GetMapping("/login/page")
    public String loginPage(Model model) {
        String location = kakaoAuthService.getLoginUrl();
        model.addAttribute("location", location);
        return "kakaoLogin";
    }

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code) {
        KakaoTokenResponseDto kakaoTokenResponseDto = kakaoAuthService.getTokenInfo(code);
        String userId = kakaoAuthService.getUserId(kakaoTokenResponseDto.getAccessToken());
        kakaoAuthService.kakaoUserLogin(userId, kakaoTokenResponseDto);
        return "redirect:/api/admin/user/list";
    }
}

package gift.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/kakaoAuth")
public class KakaoAuthController {

    @Value("${kakao.client_id}")
    private String kakaoClientId;

    @Value("${kakao.redirect_uri}")
    private String kakaoRedirectUri;

    @GetMapping("/login/page")
    public String loginPage(Model model) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+kakaoClientId+"&redirect_uri="+kakaoRedirectUri;
        model.addAttribute("location", location);
        return "kakaoLogin";
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        return ResponseEntity.ok("인가 코드 받기 성공");
    }
}

package gift.kakao;

import gift.user.domain.User;
import gift.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.Map;

@Service
public class KakaoAuthService {
    private final String clientId;
    private final String redirectUri;
    private final RestClient restClient;
    private final UserRepository userRepository;

    public KakaoAuthService(@Value("${kakao.client_id}") String clientId, @Value("${kakao.redirect_uri}") String redirectUri, UserRepository userRepository) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(3));
        requestFactory.setReadTimeout(Duration.ofSeconds(7));

        this.restClient = RestClient.builder()
                .requestFactory(requestFactory)
                .build();

        this.userRepository = userRepository;
    }

    @Transactional
    public String getAccessToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        KakaoTokenResponseDto kakaoTokenResponseDto = restClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
                .body(KakaoTokenResponseDto.class);

        return kakaoTokenResponseDto.getAccessToken();
    }

    @Transactional
    public String getUserId(String accessToken) {
        Map response = restClient.get()
                .uri("https://kapi.kakao.com/v1/user/access_token_info")
                .header(HttpHeaders.AUTHORIZATION, " Bearer " + accessToken)
                .retrieve()
                .body(Map.class);

        return response.get("id").toString();
    }

    @Transactional
    public User kakaoUserLogin(String id) {
        String email = id + "@kakao.com";

        return userRepository.findByEmail(email)
                .orElseGet(()->{
                    User user = new User(email, " ", " ");
                    return userRepository.save(user);
                });
    }

    @Transactional
    public String getLoginUrl() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("kauth.kakao.com")
                .path("/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .toUriString();
    }
}

package gift.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoAuthService {
    private final String clientId;
    private final String redirectUri;

    private final RestClient restClient;

    public KakaoAuthService(@Value("${kakao.client_id}") String clientId, @Value("${kakao.redirect_uri}") String redirectUri) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.restClient = RestClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .build();
    }

    public String getAccessToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        KakaoTokenResponseDto kakaoTokenResponseDto = restClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
                .body(KakaoTokenResponseDto.class);

        return kakaoTokenResponseDto.getAccessToken();
    }

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

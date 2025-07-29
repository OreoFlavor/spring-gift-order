package gift.kakao;

import gift.common.exception.RefreshTokenExpiredException;
import gift.user.domain.User;
import gift.user.repository.UserRepository;
import gift.user.service.UserService;
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
import java.time.Instant;
import java.util.Map;

@Service
public class KakaoAuthService {
    private final String clientId;
    private final String redirectUri;
    private final RestClient restClient;
    private final UserService userService;
    private final UserRepository userRepository;

    public KakaoAuthService(@Value("${kakao.client_id}") String clientId, @Value("${kakao.redirect_uri}") String redirectUri, UserService userService, UserRepository userRepository) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(3));
        requestFactory.setReadTimeout(Duration.ofSeconds(7));

        this.restClient = RestClient.builder()
                .requestFactory(requestFactory)
                .build();

        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Transactional
    public KakaoTokenResponseDto getTokenInfo(String code) {
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

        return kakaoTokenResponseDto;
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
    public User kakaoUserLogin(String id, KakaoTokenResponseDto kakaoTokenResponseDto) {
        String email = id + "@kakao.com";

        return userRepository.findByEmail(email)
                .orElseGet(()->{
                    KakaoUserSaveRequestDto kakaoUserSaveRequestDto = new KakaoUserSaveRequestDto(email, "default", kakaoTokenResponseDto.getAccessToken(), kakaoTokenResponseDto.refreshToken, Instant.now().plusSeconds(kakaoTokenResponseDto.getExpiresIn()), Instant.now().plusSeconds(kakaoTokenResponseDto.getRefreshTokenExpiresIn()));
                    return userService.createKakaoUser(kakaoUserSaveRequestDto);
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

    @Transactional
    public void updateToken(User user) {
        if (Instant.now().isAfter(user.getRefreshTokenExpiredAt())) {
            throw new RefreshTokenExpiredException("재로그인이 필요합니다.");
        }
        else if (Instant.now().isAfter(user.getAccessTokenExpiredAt())) {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "refresh_token");
            params.add("client_id", clientId);
            params.add("refresh_token", user.getRefreshToken());

            KakaoTokenResponseDto kakaoTokenResponseDto = restClient.post()
                    .uri("https://kauth.kakao.com/oauth/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(params)
                    .retrieve()
                    .body(KakaoTokenResponseDto.class);

            if (Instant.now().plusSeconds(2764800).isBefore(user.getRefreshTokenExpiredAt())) { //리프레시 갱신 불가
                KakaoUserPatchRequestDto kakaoUserPatchRequestDto = new KakaoUserPatchRequestDto(user.getEmail(), user.getPassword(), kakaoTokenResponseDto.accessToken, user.getRefreshToken(), Instant.now().plusSeconds(kakaoTokenResponseDto.getExpiresIn()), user.getRefreshTokenExpiredAt());
                userService.updateKakaoUser(user.getId(), kakaoUserPatchRequestDto);
            }
            else { //리프레시 갱신 가능(만료 한달 이내)
                KakaoUserPatchRequestDto kakaoUserPatchRequestDto = new KakaoUserPatchRequestDto(user.getEmail(), user.getPassword(), kakaoTokenResponseDto.accessToken, kakaoTokenResponseDto.refreshToken, Instant.now().plusSeconds(kakaoTokenResponseDto.getExpiresIn()), Instant.now().plusSeconds(kakaoTokenResponseDto.getRefreshTokenExpiresIn()));
                userService.updateKakaoUser(user.getId(), kakaoUserPatchRequestDto);
            }
        }
    }
}

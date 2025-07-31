package gift.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gift.product.dto.ProductOrderRequestDto;
import gift.user.domain.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Service
public class KakaoMessageService {

    private final RestClient restClient;
    private final KakaoAuthService kakaoAuthService;

    public KakaoMessageService(KakaoAuthService kakaoAuthService) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(3));
        requestFactory.setReadTimeout(Duration.ofSeconds(7));

        this.restClient = RestClient.builder()
                .requestFactory(requestFactory)
                .build();

        this.kakaoAuthService = kakaoAuthService;
    }

    @Transactional
    public void sendKakaoOrderMessage(User user, Long productId, ProductOrderRequestDto productOrderRequestDto) throws JsonProcessingException {
        kakaoAuthService.updateToken(user);

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode templateObject = objectMapper.createObjectNode();
        templateObject.put("object_type", "text");
        templateObject.put("text", "상품 주문 완료되었습니다. " + productOrderRequestDto.getMessage());

        ObjectNode link = objectMapper.createObjectNode();
        link.put("web_url", "http://localhost:8080/api/product/" + productId);
        link.put("mobile_web_url", "http://localhost:8080/api/product/" + productId);

        templateObject.set("link", link);
        templateObject.put("button_title", "주문 상품 확인");

        String jsonString = objectMapper.writeValueAsString(templateObject);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", jsonString);

        restClient.post()
                .uri("https://kapi.kakao.com/v2/api/talk/memo/default/send")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + user.getAccessToken())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}

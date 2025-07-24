package gift;

import gift.product.dto.ProductOptionSaveRequestDto;
import gift.product.dto.ProductSaveRequestDto;
import gift.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class paginationTest {
    @LocalServerPort
    private int port;

    private RestClient restClient = RestClient.builder().build();

    @Autowired
    ProductService productService;

    @BeforeEach
    void setUp() {
        List<ProductOptionSaveRequestDto> options = new ArrayList<>();
        options.add(new ProductOptionSaveRequestDto("option1", 100));
        for (int i = 1; i <= 21; i++) {
            String name = "testProduct" + i;
            int price = i * 1000;
            String imageUrl = "imageUrl" + i;

            ProductSaveRequestDto productSaveRequestDto = new ProductSaveRequestDto(name, price, imageUrl, options);
            productService.createProduct(productSaveRequestDto);
        }
    }

    @Test
    void 페이징_테스트() {
        String url = "http://localhost:" + port + "/api/product/page";
        ResponseEntity<Map> response = restClient
                .get()
                .uri(url)
                .retrieve()
                .toEntity(Map.class);

        Map<String, Object> body = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<LinkedHashMap<String, Object>> content = (List<LinkedHashMap<String, Object>>) body.get("content");
        assertThat(content.size()).isLessThanOrEqualTo(10);
    }


}

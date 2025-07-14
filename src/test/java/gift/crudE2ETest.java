package gift;

import gift.product.dto.ProductOptionSaveRequestDto;
import gift.product.dto.ProductPatchRequestDto;
import gift.product.dto.ProductSaveRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class crudE2ETest {
    @LocalServerPort
    private int port;
    private Long lastId;

    private RestClient restClient = RestClient.builder().build();

    @Autowired
    ProductService productService;

    @BeforeEach
    void setUp() {
        List<ProductOptionSaveRequestDto> options = new ArrayList<>();
        options.add(new ProductOptionSaveRequestDto("option1", 100));
        ProductSaveRequestDto productSaveRequestDto = new ProductSaveRequestDto("testProduct1", 1000, "imageUrl1", options);
        productService.createProduct(productSaveRequestDto);
        lastId = productService.findAll().getLast().getId();
    }

    @Test
    void 상품이_정상적으로_생성됨() {
        String url = "http://localhost:" + port + "/api/product/add";

        List<ProductOptionSaveRequestDto> options = new ArrayList<>();
        options.add(new ProductOptionSaveRequestDto("option2", 200));
        ProductSaveRequestDto productSaveRequestDto = new ProductSaveRequestDto("testProduct2", 2000, "imageUrl2", options);
        ResponseEntity<ProductResponseDto> response = restClient
                .post()
                .uri(url)
                .body(productSaveRequestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody())
                .extracting("name", "price", "imageUrl")
                .containsExactly(productSaveRequestDto.getName(), productSaveRequestDto.getPrice(), productSaveRequestDto.getImageUrl());
    }

    @Test
    void 상품_생성요청에서_이름에_특수문자를_포함할_시_400_반환() {
        String url = "http://localhost:" + port + "/api/product/add";


        List<ProductOptionSaveRequestDto> options = new ArrayList<>();
        options.add(new ProductOptionSaveRequestDto("option2", 200));
        ProductSaveRequestDto productSaveRequestDto = new ProductSaveRequestDto("testProduct2!?", 2000, "imageUrl2", options);
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () -> restClient
                                .post()
                                .uri(url)
                                .body(productSaveRequestDto)
                                .retrieve()
                                .toEntity(String.class)
                )
                .extracting(RestClientResponseException::getResponseBodyAsString)
                .asString()
                .isEqualTo("name : 알파벳, 한글, 숫자, (, ), [, ], +, -, &, /, _ 만 입력 가능합니다\n");
    }

    @Test
    void 상품이_정상적으로_조회() {
        String url = "http://localhost:" + port + "/api/product/" + lastId;
        ResponseEntity<ProductResponseDto> response = restClient
                .get()
                .uri(url)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getBody())
                .extracting("name", "price", "imageUrl")
                .containsExactly("testProduct1", 1000, "imageUrl1");
    }

    @Test
    void 존재하지_않는_상품에_대한_조회요청_시_404_반환() {
        String url = "http://localhost:" + port + "/api/product/" + "0";
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () -> restClient
                                .get()
                                .uri(url)
                                .retrieve()
                                .toEntity(String.class)
                )
                .extracting(RestClientResponseException::getResponseBodyAsString)
                .asString()
                .isEqualTo("해당 ID가 존재하지 않습니다.");
    }

    @Test
    void 상품이_정상적으로_수정() {
        String url = "http://localhost:" + port + "/api/product/" + lastId + "/update";
        ProductPatchRequestDto productPatchRequestDto = new ProductPatchRequestDto("updatedName", 10, "updatedUrl");
        ResponseEntity<ProductResponseDto> response = restClient
                .patch()
                .uri(url)
                .body(productPatchRequestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getBody())
                .extracting("name", "price", "imageUrl")
                .containsExactly("updatedName", 10, "updatedUrl");
    }

    @Test
    void 존재하지_않는_상품에_대해_수정요청_시_404_반환() {
        String url = "http://localhost:" + port + "/api/product/0/update";
        ProductPatchRequestDto productPatchRequestDto = new ProductPatchRequestDto("updatedName", 10, "updatedUrl");
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () -> restClient
                                .patch()
                                .uri(url)
                                .body(productPatchRequestDto)
                                .retrieve()
                                .toEntity(String.class)
                )
                .extracting(RestClientResponseException::getResponseBodyAsString)
                .asString()
                .isEqualTo("해당 ID가 존재하지 않습니다.");
    }

    @Test
    void 상품이_정상적으로_삭제() {
        String url = "http://localhost:" + port + "/api/product/" + lastId + "/delete";
        ResponseEntity<ProductResponseDto> response = restClient
                .delete()
                .uri(url)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void 존재하지_않는_상품에_대한_삭제요청_시_404_반환() {
        String url = "http://localhost:" + port + "/api/product/0/delete";
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () -> restClient
                                .delete()
                                .uri(url)
                                .retrieve()
                                .toEntity(String.class)
                )
                .extracting(RestClientResponseException::getResponseBodyAsString)
                .asString()
                .isEqualTo("해당 ID가 존재하지 않습니다.");
    }
}

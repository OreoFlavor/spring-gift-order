package gift.product.controller.api;

import gift.product.domain.Product;
import gift.product.dto.ProductPatchRequestDto;
import gift.product.dto.ProductSaveRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/list")
    public ResponseEntity<List<ProductResponseDto>> findAll() {
        List<ProductResponseDto> productResponseDtoList = productService.findAll()
                .stream()
                .map(ProductResponseDto::new)
                .toList();
        return ResponseEntity.ok(productResponseDtoList);
    }

    @GetMapping("/product/page")
    public ResponseEntity<Page<ProductResponseDto>> findAllByPage(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
            ) {
        Page<ProductResponseDto> responseDtoPage = productService.findAllByPage(pageable)
                .map(ProductResponseDto::new);
        return ResponseEntity.ok(responseDtoPage);
    }

    @PostMapping("/product/add")
    public ResponseEntity<ProductResponseDto> saveProduct(@RequestBody @Valid ProductSaveRequestDto productSaveRequestDto) {
        Product product =  productService.createProduct(productSaveRequestDto);
        return ResponseEntity
                .created(URI.create("/api/product/" + product.getId()))
                .body(new ProductResponseDto(product));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductResponseDto> findById(@PathVariable Long id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(new ProductResponseDto(product));
    }

    @PatchMapping("/product/{id}/update")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductPatchRequestDto productPatchRequestDto) {
        Product product = productService.updateProduct(id, productPatchRequestDto);
        return ResponseEntity.ok(new ProductResponseDto(product));
    }

    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}

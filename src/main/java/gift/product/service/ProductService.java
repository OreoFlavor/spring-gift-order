package gift.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.kakao.KakaoAuthService;
import gift.kakao.KakaoMessageService;
import gift.product.domain.Product;
import gift.product.domain.ProductOption;
import gift.product.dto.*;
import gift.product.repository.ProductOptionRepository;
import gift.product.repository.ProductRepository;
import gift.user.domain.User;
import gift.wishlist.Wishlist;
import gift.wishlist.WishlistService;
import jakarta.persistence.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    public ProductService(ProductRepository productRepository, ProductOptionRepository productOptionRepository) {
        this.productRepository = productRepository;
        this.productOptionRepository = productOptionRepository;
    }

    @Transactional
    public Product createProduct(ProductSaveRequestDto productSaveRequestDto) {
        List<ProductOption> options = productSaveRequestDto.getOptions()
                .stream()
                .map(productOptionSaveRequestDto -> new ProductOption(productOptionSaveRequestDto.getName(), productOptionSaveRequestDto.getQuantity()))
                .toList();

        Product product = new Product(productSaveRequestDto.getName(), productSaveRequestDto.getPrice(), productSaveRequestDto.getImageUrl());
        for(ProductOption productOption : options) {
            product.addOption(productOption);
        }
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Page<Product> findAllByPage(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("해당 ID가 존재하지 않습니다."));
    }

    @Transactional
    public Product updateProduct(Long id, ProductPatchRequestDto productPatchRequestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("해당 ID가 존재하지 않습니다."));
        return productRepository.save(new Product(product.getId(), productPatchRequestDto.getName(), productPatchRequestDto.getPrice(), productPatchRequestDto.getImageUrl()));
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("해당 ID가 존재하지 않습니다."));

        productRepository.delete(product);
    }
}

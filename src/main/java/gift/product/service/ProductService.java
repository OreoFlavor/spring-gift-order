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
    private final WishlistService wishlistService;
    private final KakaoMessageService kakaoMessageService;


    public ProductService(ProductRepository productRepository, ProductOptionRepository productOptionRepository, WishlistService wishlistService, KakaoMessageService kakaoMessageService) {
        this.productRepository = productRepository;
        this.productOptionRepository = productOptionRepository;
        this.wishlistService = wishlistService;
        this.kakaoMessageService = kakaoMessageService;
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

    @Transactional
    public ProductOption addOptionToProduct(Long productId, ProductOptionSaveRequestDto productOptionSaveRequestDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));

        ProductOption newOption = new ProductOption(productOptionSaveRequestDto.getName(), productOptionSaveRequestDto.getQuantity());

        product.addOption(newOption);
        return productOptionRepository.save(newOption);
    }

    @Transactional
    public void decreaseOptionQuantity(Long optionId, Integer quantity) {
        ProductOption option = productOptionRepository.findById(optionId)
                .orElseThrow(() -> new EntityNotFoundException("옵션을 찾을 수 없습니다."));

        option.decreaseQuantity(quantity);
    }

    @Transactional(readOnly = true)
    public ProductOption getOption(Long optionId) {
        return productOptionRepository.findById(optionId)
                .orElseThrow(() -> new EntityNotFoundException("옵션을 찾을 수 없습니다."));
    }

    @Transactional
    public ProductOrderResponseDto orderProduct(User user, Long productId, ProductOrderRequestDto productOrderRequestDto) throws JsonProcessingException {
        Optional<Wishlist> wishlistFound = wishlistService.getWishlistById(user.getId()).stream()
                .filter(wishlist -> wishlist.getProduct().getId().equals(productId))
                .findFirst();
        if (wishlistFound.isPresent()) {
            Long wishlistId = wishlistFound.get().getId();
            wishlistService.deleteWishlist(wishlistId);
        }

        this.decreaseOptionQuantity(productOrderRequestDto.getOptionId(), productOrderRequestDto.getQuantity());

        kakaoMessageService.sendKakaoOrderMessage(user, productId, productOrderRequestDto);

        return new ProductOrderResponseDto(productId, productOrderRequestDto.getOptionId(), productOrderRequestDto.getQuantity(), Instant.now().truncatedTo(ChronoUnit.SECONDS), productOrderRequestDto.getMessage());
    }
}

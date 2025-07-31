package gift.product.service;

import gift.product.domain.Product;
import gift.product.domain.ProductOption;
import gift.product.dto.ProductOptionSaveRequestDto;
import gift.product.repository.ProductOptionRepository;
import gift.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductOptionService {

    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;

    public ProductOptionService(ProductOptionRepository productOptionRepository, ProductRepository productRepository) {
        this.productOptionRepository = productOptionRepository;
        this.productRepository = productRepository;
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
}

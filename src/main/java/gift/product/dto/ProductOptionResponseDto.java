package gift.product.dto;

import gift.product.domain.ProductOption;

public class ProductOptionResponseDto {
    private Long id;
    private String name;
    private Integer quantity;
    private Long productId; // product 전체가 아니라 id만 둠으로써 순환 참조 차단

    protected ProductOptionResponseDto() {

    }

    public ProductOptionResponseDto(ProductOption option) {
        this.id = option.getId();
        this.name = option.getName();
        this.quantity = option.getQuantity();
        this.productId = option.getProduct().getId();
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public Integer getQuantity() { return quantity; }

    public Long getProductId() { return productId; }
}

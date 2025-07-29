package gift.product.dto;

import java.time.Instant;

public class ProductOrderResponseDto {
    private Long productId;
    private Long optionId;
    private Integer quantity;
    private Instant orderDateTime;
    private String message;

    protected ProductOrderResponseDto() {}

    public ProductOrderResponseDto(Long productId, Long optionId, Integer quantity, Instant orderDateTime, String message) {
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }
    public Long getProductId() {
        return productId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Instant getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

}

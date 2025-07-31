package gift.order.dto;

import java.time.Instant;

public class OrderResponseDto {
    private final Long productId;
    private final Long optionId;
    private final Integer quantity;
    private final Instant orderDateTime;
    private final String message;

    public OrderResponseDto(Long productId, Long optionId, Integer quantity, Instant orderDateTime, String message) {
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


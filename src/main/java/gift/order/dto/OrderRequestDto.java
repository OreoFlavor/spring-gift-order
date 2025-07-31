package gift.order.dto;

public class OrderRequestDto {
    private final Long productId;

    private final Long optionId;

    private final Integer quantity;

    private final String message;

    public OrderRequestDto(Long productId, Long optionId, Integer quantity, String message) {
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
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

    public String getMessage() {
        return message;
    }

}

package gift.product.dto;

public class ProductOrderRequestDto {
    private Long optionId;

    private Integer quantity;

    private String message;

    protected ProductOrderRequestDto() {}

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

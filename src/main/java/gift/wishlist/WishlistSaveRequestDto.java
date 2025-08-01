package gift.wishlist;

public class WishlistSaveRequestDto {

    private final Long productId;

    public WishlistSaveRequestDto(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}

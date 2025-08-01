package gift.wishlist;

public class WishlistResponseDto {
    private final Long id;

    private final Long userId;

    private final Long productId;

    public WishlistResponseDto(Long id, Long userId, Long productId) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getProductId() {
        return productId;
    }
}

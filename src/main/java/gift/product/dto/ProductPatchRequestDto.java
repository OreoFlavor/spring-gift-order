package gift.product.dto;

import gift.common.annotation.BannedWord;
import gift.common.annotation.NoSpecialChar;
import gift.product.domain.Product;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class ProductPatchRequestDto {
    @Size(min = 1, max = 15)
    @NoSpecialChar
    @BannedWord(words = {"카카오"})
    private String name;
    @PositiveOrZero
    private Integer price;
    private String imageUrl;

    protected ProductPatchRequestDto() {}

    public ProductPatchRequestDto(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
    }

    public ProductPatchRequestDto(String name, Integer price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

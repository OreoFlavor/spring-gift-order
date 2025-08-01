package gift.product.dto;

import gift.common.annotation.BannedWord;
import gift.common.annotation.NoSpecialChar;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class ProductSaveRequestDto {
    @NotNull(message = "상품명은 필수입니다.")
    @Size(min = 1, max = 15)
    @NoSpecialChar
    @BannedWord(words = {"카카오"})
    private String name;
    @NotNull(message = "가격은 필수입니다.")
    @PositiveOrZero
    private Integer price;
    private String imageUrl;

    @Size(min = 1, message = "1개 이상의 옵션은 필수입니다.")
    private List<ProductOptionSaveRequestDto> options = new ArrayList<>();

    public ProductSaveRequestDto() {}
  
    public ProductSaveRequestDto(String name, Integer price, String imageUrl, List<ProductOptionSaveRequestDto> options) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.options = options;
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

    public List<ProductOptionSaveRequestDto> getOptions() {
        return options;
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

    public void setOptions(List<ProductOptionSaveRequestDto> options) {
        this.options = options;
    }
}

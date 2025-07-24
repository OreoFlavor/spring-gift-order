package gift.product.dto;

import jakarta.validation.constraints.*;

public class ProductOptionSaveRequestDto {
    @NotBlank
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[\\w\\s\\(\\)\\[\\]\\+\\-\\&\\/_]*$", message = "허용되지 않은 특수 문자입니다.")
    private String name;

    @NotNull
    @Min(value = 1, message = "수량은 최소 1 이상입니다.")
    @Max(value = 100_000_000, message = "수량은 1억 미만입니다.")
    private Integer quantity;

    public ProductOptionSaveRequestDto(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

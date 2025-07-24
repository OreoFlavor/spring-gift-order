package gift.product.domain;

import jakarta.persistence.*;


@Entity
@Table(name = "product_option")
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    protected ProductOption() {}

    public ProductOption(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public void decreaseQuantity(Integer amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("차감 수량은 1 이상이어야 합니다.");
        }
        if (quantity < amount) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        this.quantity -= amount;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }
}

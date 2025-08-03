package gift.order.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long optionId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Instant orderDateTime;

    private String message;

    protected Order() {}

    public Order(Long productId, Long optionId, Integer quantity, Instant orderDateTime, String message) {
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public Long getId() {
        return id;
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

package gift;

import gift.product.domain.Product;
import gift.product.domain.ProductOption;
import gift.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    void save() {
        List<ProductOption> options = new ArrayList<>();
        options.add(new ProductOption("option1", 100));
        Product expected = new Product("선풍기", 1000, "", new ArrayList<>());
        expected.addOption(options.getFirst());
        Product actual = productRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
                () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl())
        );
    }
}
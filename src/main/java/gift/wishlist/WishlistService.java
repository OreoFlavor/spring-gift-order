package gift.wishlist;

import gift.product.domain.Product;
import gift.product.repository.ProductRepository;
import gift.user.domain.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WishlistService {
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Wishlist> getWishlistById(Long userId) {
        return wishRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Page<Wishlist> getWishlistByIdAndPage(Long userId, Pageable pageable) {
        return wishRepository.findByUserId(userId, pageable);
    }

    @Transactional
    public Wishlist createWishlist(User user, WishlistSaveRequestDto wishlistSaveRequestDto) {
        Product product = productRepository.findById(wishlistSaveRequestDto.getProductId())
                .orElseThrow(()-> new EntityNotFoundException("해당 ID가 존재하지 않습니다."));
        Wishlist wishlist = new Wishlist(user, product);
        return wishRepository.save(wishlist);
    }

    @Transactional
    public void deleteWishlist(Long id) {
        Wishlist wishlist = wishRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("해당 ID가 존재하지 않습니다."));
        wishRepository.delete(wishlist);
    }
}

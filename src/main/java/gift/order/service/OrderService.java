package gift.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.kakao.KakaoMessageService;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.product.service.ProductOptionService;
import gift.product.service.ProductService;
import gift.user.domain.User;
import gift.wishlist.Wishlist;
import gift.wishlist.WishlistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class OrderService {
    private final WishlistService wishlistService;
    private final ProductOptionService productOptionService;
    private final KakaoMessageService kakaoMessageService;

    public OrderService(WishlistService wishlistService, ProductOptionService productOptionService, KakaoMessageService kakaoMessageService) {
        this.wishlistService = wishlistService;
        this.productOptionService = productOptionService;
        this.kakaoMessageService = kakaoMessageService;
    }

    @Transactional
    public OrderResponseDto orderProduct(User user, Long productId, OrderRequestDto orderRequestDto) throws JsonProcessingException {
        Optional<Wishlist> wishlistFound = wishlistService.getWishlistById(user.getId()).stream()
                .filter(wishlist -> wishlist.getProduct().getId().equals(productId))
                .findFirst();
        if (wishlistFound.isPresent()) {
            Long wishlistId = wishlistFound.get().getId();
            wishlistService.deleteWishlist(wishlistId);
        }

        productOptionService.decreaseOptionQuantity(orderRequestDto.getOptionId(), orderRequestDto.getQuantity());

        kakaoMessageService.sendKakaoOrderMessage(user, productId, orderRequestDto);

        return new OrderResponseDto(productId, orderRequestDto.getOptionId(), orderRequestDto.getQuantity(), Instant.now().truncatedTo(ChronoUnit.SECONDS), orderRequestDto.getMessage());
    }
}

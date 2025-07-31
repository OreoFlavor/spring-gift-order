package gift.wishlist;

import gift.auth.LoginUser;
import gift.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<WishlistResponseDto>> getWishlist(@LoginUser User user) {
        List<WishlistResponseDto> wishlist = wishlistService.getWishlistById(user.getId()).stream()
                .map(wish -> new WishlistResponseDto(wish.getId(), wish.getUser().getId(), wish.getProduct().getId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(wishlist);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<WishlistResponseDto>> findAllByPage(
            @LoginUser User user,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<WishlistResponseDto> responseDtoPage = wishlistService.getWishlistByIdAndPage(user.getId(), pageable)
                .map(wishlist->new WishlistResponseDto(wishlist.getId(), wishlist.getUser().getId(), wishlist.getProduct().getId()));
        return ResponseEntity.ok(responseDtoPage);
    }

    @PostMapping("/add")
    public ResponseEntity<WishlistResponseDto> addWishlist(@LoginUser User user, @RequestBody WishlistSaveRequestDto wishlistSaveRequestDto) {
        Wishlist wishlist =  wishlistService.createWishlist(user, wishlistSaveRequestDto);
        WishlistResponseDto wishlistResponseDto = new WishlistResponseDto(wishlist.getId(), wishlist.getUser().getId(), wishlist.getProduct().getId());
        return ResponseEntity
                .created(URI.create("/api/wishlist/" + wishlist.getId()))
                .body(wishlistResponseDto);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteById(@LoginUser User user, @PathVariable Long id) {
        wishlistService.deleteWishlist(id);
        return ResponseEntity.noContent().build();
    }
}

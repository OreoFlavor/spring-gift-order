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

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Wishlist>> getWishlist(@LoginUser User user) {
        List<Wishlist> wishlist = wishlistService.getWishlistById(user.getId());
        return ResponseEntity.ok(wishlist);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Wishlist>> findAllByPage(
            @LoginUser User user,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<Wishlist> responseDtoPage = wishlistService.getWishlistByIdAndPage(user.getId(), pageable);
        return ResponseEntity.ok(responseDtoPage);
    }

    @PostMapping("/add")
    public ResponseEntity<Wishlist> addWishlist(@LoginUser User user, @RequestBody WishlistSaveRequestDto wishlistSaveRequestDto) {
        Wishlist wishlist =  wishlistService.createWishlist(user, wishlistSaveRequestDto);
        return ResponseEntity
                .created(URI.create("/api/wishlist/" + wishlist.getId()))
                .body(wishlist);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteById(@LoginUser User user, @PathVariable Long id) {
        wishlistService.deleteWishlist(id);
        return ResponseEntity.noContent().build();
    }
}

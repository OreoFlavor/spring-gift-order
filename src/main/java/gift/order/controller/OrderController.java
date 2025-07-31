package gift.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.auth.LoginUser;
import gift.order.service.OrderService;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.user.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/product/order/{id}")
    public ResponseEntity<OrderResponseDto> orderProduct(@LoginUser User user, @PathVariable Long id, @RequestBody OrderRequestDto orderRequestDto) throws JsonProcessingException {
        OrderResponseDto orderResponseDto = orderService.orderProduct(user, id, orderRequestDto);

        return ResponseEntity.ok(orderResponseDto);
    }
}

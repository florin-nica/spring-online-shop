package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dto.in.OrderDtoIn;
import ro.msg.learning.shop.dto.out.OrderDtoOut;
import ro.msg.learning.shop.mapper.OrderMapper;
import ro.msg.learning.shop.service.OrderService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping("/orders")
    public OrderDtoOut createOrder(@RequestBody OrderDtoIn orderDtoIn, Principal principal) {
        return orderMapper.mapOrderToOrderDtoOut(orderService.createOrder(orderDtoIn, principal.getName()));
    }
}

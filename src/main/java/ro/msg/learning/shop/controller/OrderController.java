package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dto.AddressDto;
import ro.msg.learning.shop.dto.in.OrderDtoIn;
import ro.msg.learning.shop.dto.out.OrderDetailDtoOut;
import ro.msg.learning.shop.dto.out.OrderDtoOut;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.service.OrderService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public OrderDtoOut createOrder(@RequestBody OrderDtoIn orderDtoIn) {
        return mapOrderToOrderDtoOut(orderService.createOrder(orderDtoIn));
    }

    private OrderDtoOut mapOrderToOrderDtoOut(Order order) {
        AddressDto addressDto = mapOrderAddressToAddressDto(order);
        List<OrderDetailDtoOut> orderDetails = mapOrderToOrderDetailsDto(order);

        return new OrderDtoOut(order.getDateTime(), addressDto, orderDetails);
    }

    private List<OrderDetailDtoOut> mapOrderToOrderDetailsDto(Order order) {
        return order.getOrderDetails()
                .parallelStream()
                .map(orderDetail ->
                        new OrderDetailDtoOut(
                                orderDetail.getProduct(),
                                orderDetail.getQuantity()
                        )
                )
                .collect(Collectors.toList());
    }

    private AddressDto mapOrderAddressToAddressDto(Order order) {
        return new AddressDto(
                order.getCountry(),
                order.getCity(),
                order.getCounty(),
                order.getStreet()
        );
    }
}

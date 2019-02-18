package ro.msg.learning.shop.mapper;


import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dto.AddressDto;
import ro.msg.learning.shop.dto.in.OrderDtoIn;
import ro.msg.learning.shop.dto.out.OrderDetailDtoOut;
import ro.msg.learning.shop.dto.out.OrderDtoOut;
import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.model.Order;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {


    public Order mapOrderFromOrderDtoIn(OrderDtoIn orderDtoIn) {
        Order order = new Order();
        order.setDateTime(orderDtoIn.getDateTime());

        Address address = new Address();
        address.setCity(orderDtoIn.getAddress().getCity());
        address.setCountry(orderDtoIn.getAddress().getCountry());
        address.setCounty(orderDtoIn.getAddress().getCounty());
        address.setStreet(orderDtoIn.getAddress().getStreet());

        order.setAddress(address);

        return order;
    }

    public OrderDtoOut mapOrderToOrderDtoOut(Order order) {
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
                order.getAddress().getCountry(),
                order.getAddress().getCity(),
                order.getAddress().getCounty(),
                order.getAddress().getStreet()
        );
    }
}

package ro.msg.learning.shop.mapper;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dto.in.OrderDetailDtoIn;
import ro.msg.learning.shop.dto.in.OrderDtoIn;
import ro.msg.learning.shop.dto.out.OrderDetailDtoOut;
import ro.msg.learning.shop.dto.out.OrderDtoOut;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.odata.data.ODataOrder;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    public Order mapOrderFromOrderDtoIn(OrderDtoIn orderDtoIn) {
        Order order = new Order();
        order.setDateTime(orderDtoIn.getDateTime());
        order.setAddress(orderDtoIn.getAddress());

        return order;
    }

    public OrderDtoIn mapODataOrderToOrderDtoIn(ODataOrder oDataOrder) {
        OrderDtoIn orderDtoIn = new OrderDtoIn();

        orderDtoIn.setDateTime(oDataOrder.getDateTime());
        orderDtoIn.setAddress(oDataOrder.getAddress());

        List<OrderDetailDtoIn> orderDetails = oDataOrder.getODataOrderDetails().parallelStream()
                .map(oDataOrderDetail -> {
                    OrderDetailDtoIn orderDetailDtoIn = new OrderDetailDtoIn();
                    orderDetailDtoIn.setProductId(oDataOrderDetail.getProductId());
                    orderDetailDtoIn.setQuantity(oDataOrderDetail.getQuantity());
                    return orderDetailDtoIn;
                })
                .collect(Collectors.toList());

        orderDtoIn.setOrderDetails(orderDetails);

        return orderDtoIn;
    }

    public OrderDtoOut mapOrderToOrderDtoOut(Order order) {
        List<OrderDetailDtoOut> orderDetails = mapOrderToOrderDetailsDto(order);

        return new OrderDtoOut(order.getDateTime(), order.getAddress(), orderDetails);
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
}

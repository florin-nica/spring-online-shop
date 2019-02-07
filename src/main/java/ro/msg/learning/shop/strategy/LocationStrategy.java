package ro.msg.learning.shop.strategy;

import ro.msg.learning.shop.dto.in.OrderDtoIn;
import ro.msg.learning.shop.model.LocationProductQuantity;

import java.util.List;

public interface LocationStrategy {

    List<LocationProductQuantity> getLocationProductQuantity(OrderDtoIn orderDtoIn);

}

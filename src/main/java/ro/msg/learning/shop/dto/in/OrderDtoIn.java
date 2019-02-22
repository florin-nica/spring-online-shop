package ro.msg.learning.shop.dto.in;

import lombok.Data;
import ro.msg.learning.shop.model.Address;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDtoIn {

    private LocalDateTime dateTime;
    private Address address;
    private List<OrderDetailDtoIn> orderDetails;
}

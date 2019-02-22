package ro.msg.learning.shop.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import ro.msg.learning.shop.model.Address;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderDtoOut {

    private LocalDateTime dateTime;
    private Address address;
    private List<OrderDetailDtoOut> orderDetails;
}

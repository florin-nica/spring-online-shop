package ro.msg.learning.shop.dto.in;

import lombok.Data;
import ro.msg.learning.shop.dto.AddressDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDtoIn {

    private LocalDateTime dateTime;
    private AddressDto address;
    private List<OrderDetailDtoIn> orderDetails;
}

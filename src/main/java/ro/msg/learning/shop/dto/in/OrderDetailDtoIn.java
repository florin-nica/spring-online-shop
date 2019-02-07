package ro.msg.learning.shop.dto.in;

import lombok.Data;

@Data
public class OrderDetailDtoIn {

    private Integer productId;
    private Integer quantity;
}

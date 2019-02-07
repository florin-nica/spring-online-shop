package ro.msg.learning.shop.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import ro.msg.learning.shop.model.Product;

@Data
@AllArgsConstructor
public class OrderDetailDtoOut {

    private Product product;
    private Integer quantity;
}

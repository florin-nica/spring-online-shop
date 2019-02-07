package ro.msg.learning.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockProductQuantity {

    private Stock stock;
    private Product product;
    private Integer quantity;
}

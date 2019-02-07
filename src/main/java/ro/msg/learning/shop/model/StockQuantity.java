package ro.msg.learning.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockQuantity {

    private Stock stock;
    private Integer quantity;
}

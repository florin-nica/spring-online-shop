package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockDto {

    private Integer id;
    private Integer quantity;
    private Integer locationId;
    private Integer productId;

}

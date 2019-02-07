package ro.msg.learning.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationProductQuantity {

    private Location location;
    private Product product;
    private Integer quantity;
}

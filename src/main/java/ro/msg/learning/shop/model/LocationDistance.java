package ro.msg.learning.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationDistance {

    private Location location;
    private Integer distance;
}

package ro.msg.learning.shop.odata.data;

import lombok.Data;
import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.model.Customer;
import ro.msg.learning.shop.model.Location;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class ODataOrder {

    private Integer id;
    private Set<Location> shippedFrom;
    private Customer customer;
    private List<ODataOrderDetail> oDataOrderDetails;
    private LocalDateTime dateTime;
    private Address address;
}

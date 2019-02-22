package ro.msg.learning.shop.odata.data;

import lombok.Data;

@Data
public class ODataOrderDetail {

    private Integer id;
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
}

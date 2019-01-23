package ro.msg.learning.shop.model;

import lombok.Data;

@Data
public class OrderDetail {

    private int order;
    private int product;
    private int quantity;
}

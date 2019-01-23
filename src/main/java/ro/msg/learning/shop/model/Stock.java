package ro.msg.learning.shop.model;

import lombok.Data;

@Data
public class Stock {

    private int product;
    private int location;
    private int quantity;
}

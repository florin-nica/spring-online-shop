package ro.msg.learning.shop.model;

import lombok.Data;

@Data
public class Order {

    private int id;
    private int shippedFrom;
    private int customer;
    private String country;
    private String city;
    private String county;
    private String street;
}

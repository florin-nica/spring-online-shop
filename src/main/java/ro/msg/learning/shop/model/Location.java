package ro.msg.learning.shop.model;

import lombok.Data;

@Data
public class Location {

    private int id;
    private String name;
    private String country;
    private String city;
    private String county;
    private String street;
}

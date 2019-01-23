package ro.msg.learning.shop.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {

    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private double weight;
    private int category;
    private int supplier;
}

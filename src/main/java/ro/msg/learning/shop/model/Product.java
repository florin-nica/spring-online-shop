package ro.msg.learning.shop.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private BigDecimal price;
    private Double weight;

    @ManyToOne
    private ProductCategory category;

    @ManyToOne
    private Supplier supplier;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<Stock> stock;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<OrderDetail> orderDetails;

}

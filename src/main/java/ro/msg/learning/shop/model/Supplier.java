package ro.msg.learning.shop.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplier")
    private List<Product> products;

    private String name;
}

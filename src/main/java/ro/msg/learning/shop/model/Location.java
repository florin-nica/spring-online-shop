package ro.msg.learning.shop.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;

@Data
@Entity
class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany(mappedBy = "shippedFrom")
    private Set<Order> orders;

    private String name;
    private String country;
    private String city;
    private String county;
    private String street;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location")
    private List<Stock> stocks;


}

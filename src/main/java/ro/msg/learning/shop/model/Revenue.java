package ro.msg.learning.shop.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@SqlResultSetMapping(name = "RevenueResult",
        classes = {@ConstructorResult(targetClass = Revenue.class,
                columns = {@ColumnResult(name = "location_id", type = Integer.class),
                        @ColumnResult(name = "date", type = LocalDate.class),
                        @ColumnResult(name = "sum", type = BigDecimal.class)})
        })
@NamedNativeQuery(name = "Revenue.getSummedRevenuesByLocationAndDate",
        query = "SELECT ol.location_id, CAST(o.date_time as date) as date, SUM(od.quantity * p.price) AS sum " +
                "FROM order_detail od\n" +
                "JOIN orders o ON od.order_id = o.id    \n" +
                "JOIN order_location ol ON o.id = ol.order_id    \n" +
                "JOIN Product p ON od.product_id = p.id     \n" +
                "WHERE o.date_time BETWEEN :startTime AND :endTime    \n" +
                "GROUP BY ol.location_id, date",
        resultSetMapping = "RevenueResult")
public class Revenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer locationId;

    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;

    private BigDecimal sum;

    public Revenue(Integer locationId, LocalDate date, BigDecimal sum) {
        this.locationId = locationId;
        this.date = date;
        this.sum = sum;
    }
}

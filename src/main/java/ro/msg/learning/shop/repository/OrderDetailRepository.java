package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.msg.learning.shop.model.OrderDetail;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    @Query(value = "SELECT ol.location_id, CAST(o.date_time as date) as date, SUM(od.quantity * p.price) AS sum " +
            "FROM order_detail od\n" +
            "JOIN orders o ON od.order_id = o.id    \n" +
            "JOIN order_location ol ON o.id = ol.order_id    \n" +
            "JOIN Product p ON od.product_id = p.id     \n" +
            "WHERE o.date_time BETWEEN ?1 AND ?2    \n" +
            "GROUP BY ol.location_id, date",
            nativeQuery = true)
    List<Object[]> getSummedRevenuesByLocationAndDate(LocalDateTime start, LocalDateTime end);
}

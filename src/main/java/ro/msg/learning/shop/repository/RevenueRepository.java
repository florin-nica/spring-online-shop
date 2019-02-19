package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.msg.learning.shop.model.Revenue;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Integer> {

    List<Revenue> getSummedRevenuesByLocationAndDate(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}

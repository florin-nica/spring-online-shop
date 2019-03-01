package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.msg.learning.shop.model.Revenue;
import ro.msg.learning.shop.repository.RevenueRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RevenueService {

    private final RevenueRepository revenueRepository;

    public void saveDailyRevenues(LocalDate date) {
        LocalDateTime startTime = date.atStartOfDay();
        LocalDate nextDay = date.plusDays(1);

        List<Revenue> dailyRevenues =
                revenueRepository.getSummedRevenuesByLocationAndDate(startTime, nextDay);

        revenueRepository.saveAll(dailyRevenues);
    }
}

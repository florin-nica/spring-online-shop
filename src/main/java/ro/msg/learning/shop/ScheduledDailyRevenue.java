package ro.msg.learning.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.service.RevenueService;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ScheduledDailyRevenue {

    private final RevenueService revenueService;

    @Scheduled(cron = "0 0 0 * * *")
    public void computeDailyRevenueForAllLocations() {
        revenueService.saveDailyRevenues(LocalDate.now().minusDays(1));
    }
}

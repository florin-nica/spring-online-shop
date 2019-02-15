package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.model.Revenue;
import ro.msg.learning.shop.repository.OrderDetailRepository;
import ro.msg.learning.shop.repository.RevenueRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RevenueService {

    private final RevenueRepository revenueRepository;
    private final OrderDetailRepository orderDetailRepository;

    public void saveDailyRevenues(LocalDate date) {
        LocalDateTime startTime = date.atStartOfDay();
        LocalDateTime endTime = date.atTime(23, 59, 59);

        List<Object[]> revenues =
                orderDetailRepository.getSummedRevenuesByLocationAndDate(startTime, endTime);

        List<Revenue> dailyRevenues =
                revenues.parallelStream()
                        .map(objects -> Revenue.builder()
                                .locationId((Integer) objects[0])
                                .date(convertSQLDateToLocalDate((Date) objects[1]))
                                .sum((BigDecimal) objects[2])
                                .build()
                        )
                        .collect(Collectors.toList());

        revenueRepository.saveAll(dailyRevenues);
    }

    private LocalDate convertSQLDateToLocalDate(Date date) {
        return date == null ? null : Jsr310Converters.DateToLocalDateConverter.INSTANCE.convert(date);
    }
}

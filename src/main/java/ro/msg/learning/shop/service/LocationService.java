package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.msg.learning.shop.dto.in.OrderDtoIn;
import ro.msg.learning.shop.model.LocationProductQuantity;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.strategy.LocationStrategy;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationService {

    private final LocationStrategy locationStrategy;
    private final StockService stockService;

    public List<LocationProductQuantity> getLocationsWithProductsByOrder(OrderDtoIn orderDtoIn) {
        return locationStrategy.getLocationProductQuantity(orderDtoIn);
    }

    public List<Stock> getStocksByLocationAndProductIds(List<LocationProductQuantity> locationProductQuantities) {
        return stockService.getStocksByLocationAndProductIds(locationProductQuantities);
    }
}

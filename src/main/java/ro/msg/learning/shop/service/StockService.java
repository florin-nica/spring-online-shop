package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.model.LocationProductQuantity;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.model.StockProductQuantity;
import ro.msg.learning.shop.model.StockQuantity;
import ro.msg.learning.shop.repository.StockRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public List<Stock> getStocksByLocationId(Integer locationId) {
        return stockRepository.findAllByLocationId(locationId);
    }

    public void subtractOrderedStocksQuantity(List<StockProductQuantity> stockProductQuantities) {
        List<Stock> updatedStocks =
                getStocksWithOrderedQuantities(stockProductQuantities)
                        .parallelStream()
                        .map(this::getStockWithReducedQuantity)
                        .collect(Collectors.toList());

        stockRepository.saveAll(updatedStocks);
    }

    public List<Stock> getStocksByLocationAndProductIds(List<LocationProductQuantity> locationProductQuantities) {
        return stockRepository.findStocksByLocationAndProductIds(locationProductQuantities);
    }

    private Stock getStockWithReducedQuantity(StockQuantity stockQuantity) {
        Stock stock = stockQuantity.getStock();
        stock.setQuantity(stockQuantity.getStock().getQuantity() - stockQuantity.getQuantity());
        return stock;
    }

    private List<StockQuantity> getStocksWithOrderedQuantities(List<StockProductQuantity> stockProductQuantities) {
        return stockProductQuantities
                .parallelStream()
                .map(stockProductQuantity ->
                        new StockQuantity(
                                stockProductQuantity.getStock(),
                                stockProductQuantity.getQuantity()
                        )
                )
                .collect(Collectors.toList());
    }

}

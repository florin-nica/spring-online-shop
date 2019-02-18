package ro.msg.learning.shop.mapper;

import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dto.StockDto;
import ro.msg.learning.shop.model.Stock;

@Component
public class StockMapper {

    public StockDto mapStockToStockDto(Stock stock) {
        return new StockDto(
                stock.getId(),
                stock.getQuantity(),
                stock.getLocation().getId(),
                stock.getProduct().getId()
        );
    }
}

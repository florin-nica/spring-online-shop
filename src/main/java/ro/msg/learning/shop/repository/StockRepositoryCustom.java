package ro.msg.learning.shop.repository;

import ro.msg.learning.shop.model.LocationProductQuantity;
import ro.msg.learning.shop.model.Stock;

import java.util.List;

public interface StockRepositoryCustom {

    List<Stock> findStocksByLocationAndProductIds(List<LocationProductQuantity> locationProductQuantities);
}

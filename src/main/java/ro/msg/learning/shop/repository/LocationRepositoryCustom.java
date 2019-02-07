package ro.msg.learning.shop.repository;

import java.util.List;
import java.util.Map;

public interface LocationRepositoryCustom {

    List<Integer> findLocationsWithAllProductsInStock(Map<Integer, Integer> productsWithQuantity);
}

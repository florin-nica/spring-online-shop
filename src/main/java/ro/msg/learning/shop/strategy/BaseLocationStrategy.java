package ro.msg.learning.shop.strategy;

import lombok.RequiredArgsConstructor;
import ro.msg.learning.shop.dto.in.OrderDetailDtoIn;
import ro.msg.learning.shop.dto.in.OrderDtoIn;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BaseLocationStrategy {

    private final LocationRepository locationRepository;
    private final ProductRepository productRepository;

    protected Map<Integer, Integer> productIdQuantityMap;
    protected List<Location> locationsWithAllProducts;
    protected List<Product> orderedProducts;

    protected void setLocationsWithAllProductsAndOrderedProducts(OrderDtoIn orderDtoIn) {
        orderedProducts = new ArrayList<>();

        productIdQuantityMap =
                orderDtoIn.getOrderDetails()
                        .parallelStream()
                        .collect(Collectors.toMap(OrderDetailDtoIn::getProductId, OrderDetailDtoIn::getQuantity));

        List<Integer> locationIds = locationRepository.findLocationsWithAllProductsInStock(productIdQuantityMap);

        if (!locationIds.isEmpty()) {
            locationsWithAllProducts = locationRepository.findAllById(locationIds);
            orderedProducts.addAll(productRepository.findAllById(productIdQuantityMap.keySet()));
        }
    }
}

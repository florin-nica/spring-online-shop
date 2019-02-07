package ro.msg.learning.shop.strategy;

import lombok.RequiredArgsConstructor;
import ro.msg.learning.shop.dto.in.OrderDtoIn;
import ro.msg.learning.shop.exception.StockNotFoundException;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.LocationProductQuantity;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SingleLocationStrategy implements LocationStrategy {

    private final LocationRepository locationRepository;
    private final ProductRepository productRepository;

    @Override
    public List<LocationProductQuantity> getLocationProductQuantity(OrderDtoIn orderDtoIn) {

        Map<Integer, Integer> productIdQuantityMap = new HashMap<>();
        List<Product> orderedProducts = new ArrayList<>();
        Location locationWithAllProducts = null;

        orderDtoIn.getOrderDetails().forEach(orderDetailDto ->
                productIdQuantityMap.put(orderDetailDto.getProductId(), orderDetailDto.getQuantity())
        );

        List<Integer> locationIds = locationRepository.findLocationsWithAllProductsInStock(productIdQuantityMap);

        if (!locationIds.isEmpty()) {
            locationWithAllProducts = locationRepository.findById(locationIds.get(0)).orElse(null);
            orderedProducts.addAll(productRepository.findProductsByIds(productIdQuantityMap.keySet()));
        }

        if (locationWithAllProducts != null) {
            final Location location = locationWithAllProducts;
            return orderedProducts
                    .parallelStream()
                    .map(product -> new LocationProductQuantity(location, product, productIdQuantityMap.get(product.getId())))
                    .collect(Collectors.toList());
        }

        throw new StockNotFoundException("No stocks found");
    }
}

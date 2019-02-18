package ro.msg.learning.shop.strategy;

import ro.msg.learning.shop.dto.in.OrderDtoIn;
import ro.msg.learning.shop.exception.StockNotFoundException;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.LocationProductQuantity;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

public class SingleLocationStrategy extends BaseLocationStrategy implements LocationStrategy {

    public SingleLocationStrategy(LocationRepository locationRepository, ProductRepository productRepository) {
        super(locationRepository, productRepository);
    }

    @Override
    public List<LocationProductQuantity> getLocationProductQuantity(OrderDtoIn orderDtoIn) {

        setLocationsWithAllProductsAndOrderedProducts(orderDtoIn);

        if (locationsWithAllProducts != null && !locationsWithAllProducts.isEmpty()) {
            final Location location = locationsWithAllProducts.get(0);
            return orderedProducts
                    .parallelStream()
                    .map(product -> new LocationProductQuantity(location, product, productIdQuantityMap.get(product.getId())))
                    .collect(Collectors.toList());
        }

        throw new StockNotFoundException("No stocks found");
    }
}

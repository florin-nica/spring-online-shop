package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.msg.learning.shop.dto.in.OrderDtoIn;
import ro.msg.learning.shop.exception.CustomerNotFoundException;
import ro.msg.learning.shop.mapper.OrderMapper;
import ro.msg.learning.shop.model.Customer;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.LocationProductQuantity;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.model.OrderDetail;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.model.StockProductQuantity;
import ro.msg.learning.shop.repository.CustomerRepository;
import ro.msg.learning.shop.repository.OrderRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final LocationService locationService;
    private final StockService stockService;
    private final OrderMapper orderMapper;

    public Order createOrder(OrderDtoIn orderDtoIn, String username) {

        Order order = orderMapper.mapOrderFromOrderDtoIn(orderDtoIn);

        List<LocationProductQuantity> locationsWithProductsAndQuantities =
                locationService.getLocationsWithProductsByOrder(orderDtoIn);

        Set<Location> locations = locationsWithProductsAndQuantities.parallelStream()
                .map(LocationProductQuantity::getLocation)
                .collect(Collectors.toSet());

        Customer customer = Optional.ofNullable(customerRepository.findByUserUsername(username))
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        order.setCustomer(customer);
        order.setShippedFrom(locations);

        Map<Integer, Integer> productIdQuantityMap = new HashMap<>();
        orderDtoIn.getOrderDetails().forEach(orderDetailDto ->
                productIdQuantityMap.put(orderDetailDto.getProductId(), orderDetailDto.getQuantity())
        );

        List<Stock> stocksToUpdate = locationService.getStocksByLocationAndProductIds(locationsWithProductsAndQuantities);

        List<StockProductQuantity> stockProductQuantities =
                stocksToUpdate.parallelStream()
                        .map(stock -> new StockProductQuantity(
                                stock,
                                stock.getProduct(),
                                productIdQuantityMap.get(stock.getProduct().getId())))
                        .collect(Collectors.toList());

        stockService.subtractOrderedStocksQuantity(stockProductQuantities);
        order.setOrderDetails(getOrderDetails(order, stockProductQuantities));

        order = orderRepository.save(order);

        return order;
    }

    private List<OrderDetail> getOrderDetails(final Order order, List<StockProductQuantity> stockProductQuantity) {
        return stockProductQuantity.parallelStream()
                .map(stockProdQuantity ->
                        OrderDetail.builder()
                                .order(order)
                                .product(stockProdQuantity.getProduct())
                                .quantity(stockProdQuantity.getQuantity())
                                .build()
                )
                .collect(Collectors.toList());
    }
}

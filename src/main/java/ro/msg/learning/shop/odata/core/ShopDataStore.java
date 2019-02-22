package ro.msg.learning.shop.odata.core;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dto.in.OrderDtoIn;
import ro.msg.learning.shop.mapper.OrderMapper;
import ro.msg.learning.shop.model.Address;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.model.OrderDetail;
import ro.msg.learning.shop.model.Product;
import ro.msg.learning.shop.odata.data.ODataOrder;
import ro.msg.learning.shop.repository.OrderDetailRepository;
import ro.msg.learning.shop.repository.OrderRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.service.OrderService;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShopDataStore {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public Map<String, Object> createOrder(ODataOrder oDataOrder) {
        OrderDtoIn orderDtoIn = orderMapper.mapODataOrderToOrderDtoIn(oDataOrder);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Order savedOrder = orderService.createOrder(orderDtoIn, currentPrincipalName);
        return getOrderMap(savedOrder);
    }

    public List<Map<String, Object>> getOrders() {
        return orderRepository.findAll()
                .parallelStream()
                .map(this::getOrderMap)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getOrder(int id) {
        return orderRepository.findById(id)
                .map(this::getOrderMap)
                .orElse(Collections.emptyMap());
    }

    public List<Map<String, Object>> getProducts() {
        return productRepository.findAll()
                .parallelStream()
                .map(this::getProductMap)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getProduct(int id) {
        return productRepository.findById(id)
                .map(this::getProductMap)
                .orElse(Collections.emptyMap());
    }

    public List<Map<String, Object>> getOrderDetails() {
        return orderDetailRepository.findAll()
                .parallelStream()
                .map(this::getOrderDetailMap)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getOrderDetail(int id) {
        return orderDetailRepository.findById(id)
                .map(this::getOrderDetailMap)
                .orElse(Collections.emptyMap());
    }

    public List<Map<String, Object>> getOrderDetailsForOrder(int orderId) {
        return orderDetailRepository.findAllByOrderId(orderId)
                .parallelStream()
                .map(this::getOrderDetailMap)
                .collect(Collectors.toList());
    }

    private Map<String, Object> getOrderMap(Order order) {
        Map<String, Object> data = new HashMap<>();
        data.put("Id", order.getId());
        data.put("AddressInfo", createAddress(order.getAddress()));

        if (order.getDateTime() != null) {
            data.put("DateTime", Timestamp.valueOf(order.getDateTime()));
        }

        return data;
    }

    private Map<String, Object> getProductMap(Product product) {
        Map<String, Object> data = new HashMap<>();
        data.put("Id", product.getId());
        data.put("Name", product.getName());
        data.put("Description", product.getDescription());
        data.put("Price", product.getPrice());
        data.put("Weight", product.getWeight());
        return data;
    }

    private Map<String, Object> getOrderDetailMap(OrderDetail orderDetail) {
        Map<String, Object> data = new HashMap<>();
        if (orderDetail != null) {
            data.put("Id", orderDetail.getId());
            data.put("Product", orderDetail.getProduct());
            data.put("Order", orderDetail.getOrder());
            data.put("Quantity", orderDetail.getQuantity());
        }
        return data;
    }

    private Map<String, Object> createAddress(Address address) {
        Map<String, Object> addressObj = new HashMap<>();
        if (address != null) {
            addressObj.put("Street", address.getStreet());
            addressObj.put("City", address.getCity());
            addressObj.put("County", address.getCounty());
            addressObj.put("Country", address.getCounty());
        }
        return addressObj;
    }
}

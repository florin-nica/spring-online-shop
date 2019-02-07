package ro.msg.learning.shop.repository;

import ro.msg.learning.shop.model.Product;

import java.util.List;
import java.util.Set;

public interface ProductRepositoryCustom {

    List<Product> findProductsByIds(Set<Integer> productIds);
}

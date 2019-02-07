package ro.msg.learning.shop.repository;

import ro.msg.learning.shop.model.LocationProductQuantity;
import ro.msg.learning.shop.model.Stock;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class StockRepositoryCustomImpl implements StockRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Stock> findStocksByLocationAndProductIds(List<LocationProductQuantity> locationProductQuantities) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Stock> query = cb.createQuery(Stock.class);
        Root<Stock> stock = query.from(Stock.class);
        Path<Integer> locationIdPath = stock.get("location").get("id");
        Path<Integer> productIdPath = stock.get("product").get("id");
        List<Predicate> predicates = new ArrayList<>();

        locationProductQuantities.forEach(locationProductQuantity ->
                predicates.add(
                        cb.and(
                                cb.equal(locationIdPath, locationProductQuantity.getLocation().getId()),
                                cb.equal(productIdPath, locationProductQuantity.getProduct().getId())
                        )
                )
        );

        query.select(stock)
                .where(cb.or(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query)
                .getResultList();
    }
}

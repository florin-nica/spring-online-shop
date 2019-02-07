package ro.msg.learning.shop.repository;

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
import java.util.Map;

public class LocationRepositoryCustomImpl implements LocationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Integer> findLocationsWithAllProductsInStock(Map<Integer, Integer> productIdsWithQuantity) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
        Root<Stock> stock = query.from(Stock.class);
        Path<Integer> productIdPath = stock.get("product").get("id");
        Path<Integer> locationIdPath = stock.get("location").get("id");
        Path<Integer> quantityPath = stock.get("quantity");
        List<Predicate> predicates = new ArrayList<>();

        productIdsWithQuantity.forEach((productId, quantity) ->
                predicates.add(
                        cb.and(
                                cb.equal(productIdPath, productId),
                                cb.greaterThanOrEqualTo(quantityPath, quantity)
                        )
                ));

        query.select(locationIdPath)
                .where(cb.or(predicates.toArray(new Predicate[0])))
                .groupBy(locationIdPath)
                .having(cb.equal(cb.count(productIdPath), productIdsWithQuantity.size()));

        return entityManager.createQuery(query)
                .getResultList();
    }
}

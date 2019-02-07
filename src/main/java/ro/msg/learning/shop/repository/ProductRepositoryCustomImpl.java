package ro.msg.learning.shop.repository;

import ro.msg.learning.shop.model.Product;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> findProductsByIds(Set<Integer> productIds) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);
        Path<Integer> productIdPath = product.get("id");
        List<Predicate> predicates = new ArrayList<>();

        productIds.forEach(productId -> predicates.add(cb.equal(productIdPath, productId)));

        query.select(product)
                .where(cb.or(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query)
                .getResultList();
    }
}

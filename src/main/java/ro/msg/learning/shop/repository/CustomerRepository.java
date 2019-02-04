package ro.msg.learning.shop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.msg.learning.shop.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

}

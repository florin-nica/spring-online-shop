package ro.msg.learning.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.msg.learning.shop.model.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

}

package ma.mihradi.ebankbackend.repositories;

import ma.mihradi.ebankbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}

package ma.mihradi.ebankbackend.repositories;

import ma.mihradi.ebankbackend.entities.AccountOperation;
import ma.mihradi.ebankbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
}

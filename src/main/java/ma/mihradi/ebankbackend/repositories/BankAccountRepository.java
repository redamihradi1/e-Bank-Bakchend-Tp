package ma.mihradi.ebankbackend.repositories;

import ma.mihradi.ebankbackend.entities.BankAccount;
import ma.mihradi.ebankbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}

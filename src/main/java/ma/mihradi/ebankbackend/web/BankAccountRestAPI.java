package ma.mihradi.ebankbackend.web;


import lombok.AllArgsConstructor;
import ma.mihradi.ebankbackend.entities.BankAccount;
import ma.mihradi.ebankbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BankAccountRestAPI {
    private BankAccountService bankAccountService;


}

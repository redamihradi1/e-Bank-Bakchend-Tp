package ma.mihradi.ebankbackend;

import ma.mihradi.ebankbackend.entities.AccountOperation;
import ma.mihradi.ebankbackend.entities.CurrentAccount;
import ma.mihradi.ebankbackend.entities.Customer;
import ma.mihradi.ebankbackend.entities.SavingAccount;
import ma.mihradi.ebankbackend.enums.AccountStatus;
import ma.mihradi.ebankbackend.enums.OperationType;
import ma.mihradi.ebankbackend.repositories.AccountOperationRepository;
import ma.mihradi.ebankbackend.repositories.BankAccountRepository;
import ma.mihradi.ebankbackend.repositories.CustomerRepository;
import ma.mihradi.ebankbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankService bankService){
        return args -> {
            bankService.consulter();
        };
    }
    //@Bean
    CommandLineRunner start(
            CustomerRepository customerRepository,
            BankAccountRepository bankAccountRepository,
            AccountOperationRepository accountOperationRepository
    ){
        return args ->
        {
            Stream.of("Reda","Ayman","Driss").forEach(
                    nom->{
                        Customer customer = new Customer();
                        customer.setName(nom);
                        customer.setEmail(nom+"@gmail.com");
                        customerRepository.save(customer);
                    }
            );
            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*10000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(9000);

                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*10000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(5.5);

                bankAccountRepository.save(savingAccount);

            });
            bankAccountRepository.findAll().forEach(bankAccount -> {
                for (int i=0;i<5 ;i++){
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(bankAccount);
                    accountOperationRepository.save(accountOperation);

                }
            });
        };
    }

}

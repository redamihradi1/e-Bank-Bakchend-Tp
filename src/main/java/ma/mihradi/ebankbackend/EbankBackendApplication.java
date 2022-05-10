package ma.mihradi.ebankbackend;

import ma.mihradi.ebankbackend.entities.*;
import ma.mihradi.ebankbackend.enums.AccountStatus;
import ma.mihradi.ebankbackend.enums.OperationType;
import ma.mihradi.ebankbackend.exceptions.BalanceNotSufficentException;
import ma.mihradi.ebankbackend.exceptions.BankAccountNotFoundException;
import ma.mihradi.ebankbackend.exceptions.CustomerNotFoundException;
import ma.mihradi.ebankbackend.repositories.AccountOperationRepository;
import ma.mihradi.ebankbackend.repositories.BankAccountRepository;
import ma.mihradi.ebankbackend.repositories.CustomerRepository;
import ma.mihradi.ebankbackend.services.BankAccountService;
import ma.mihradi.ebankbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
           Stream.of("Reda","Driss","Ahmed").forEach(name->{
               Customer customer =new Customer();
               customer.setName(name);
               customer.setEmail(name+"@gmail.com");
               bankAccountService.saveCustomer(customer);
           });
           bankAccountService.listCustomers().forEach(customer -> {
               try {
                   bankAccountService.saveCurrentBankAccount(Math.random()*90000,9000,customer.getId());
                   bankAccountService.saveSavingBankAccount(Math.random()*120000,5.4,customer.getId());
                   List<BankAccount> bankAccounts = bankAccountService.bankAccountList();
                   for (BankAccount bankAccount:bankAccounts){
                       for (int i=0;i<10;i++){
                           bankAccountService.credit(bankAccount.getId(),10000+Math.random()*120000,"Credit");
                           bankAccountService.debit(bankAccount.getId(),1000+Math.random()*9000,"Debit");
                       }
                   }
               } catch (CustomerNotFoundException e) {
                   e.printStackTrace();
               } catch (BankAccountNotFoundException e) {
                   e.printStackTrace();
               } catch (BalanceNotSufficentException e) {
                   e.printStackTrace();
               }
           });
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

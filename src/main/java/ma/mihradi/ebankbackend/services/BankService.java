package ma.mihradi.ebankbackend.services;

import ma.mihradi.ebankbackend.entities.BankAccount;
import ma.mihradi.ebankbackend.entities.CurrentAccount;
import ma.mihradi.ebankbackend.entities.SavingAccount;
import ma.mihradi.ebankbackend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankService {
    @Autowired
    BankAccountRepository bankAccountRepository;
    public void consulter(){
        BankAccount bankAccount = bankAccountRepository.findById("08c4256f-b182-492e-ad7d-a38119edb58e").orElse(null);
        if (bankAccount!=null){
            System.out.println("************************************");
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getCustomer().getName());
            System.out.println(bankAccount.getClass().getSimpleName());
            if(bankAccount instanceof CurrentAccount){
                System.out.println("Over Draft == "+((CurrentAccount) bankAccount).getOverDraft());
            }
            else if(bankAccount instanceof SavingAccount){
                System.out.println("Rate == "+((SavingAccount) bankAccount).getInterestRate());
            }
            bankAccount.getAccountOperations().forEach(accountOperation -> {
                System.out.println(accountOperation.getType()+"\t"+accountOperation.getOperationDate()+"\t"+accountOperation.getAmount());
            });

        }
    }
}

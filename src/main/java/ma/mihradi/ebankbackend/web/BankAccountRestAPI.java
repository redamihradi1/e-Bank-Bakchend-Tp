package ma.mihradi.ebankbackend.web;


import lombok.AllArgsConstructor;
import ma.mihradi.ebankbackend.dtos.AccountHistoryDto;
import ma.mihradi.ebankbackend.dtos.BankAccountDTO;
import ma.mihradi.ebankbackend.entities.AccountOperationDTO;
import ma.mihradi.ebankbackend.entities.BankAccount;
import ma.mihradi.ebankbackend.exceptions.BankAccountNotFoundException;
import ma.mihradi.ebankbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BankAccountRestAPI {
    private BankAccountService bankAccountService;


    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccount(){
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable  String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDto getAccountHistory(@PathVariable  String accountId,
                                               @RequestParam(name = "page",defaultValue ="0" ) int page,
                                               @RequestParam(name = "size",defaultValue ="5" ) int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }
}

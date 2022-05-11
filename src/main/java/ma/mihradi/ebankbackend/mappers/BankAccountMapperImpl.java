package ma.mihradi.ebankbackend.mappers;

import lombok.extern.slf4j.Slf4j;
import ma.mihradi.ebankbackend.dtos.CurrentBankAccountDTO;
import ma.mihradi.ebankbackend.dtos.CustomerDTO;
import ma.mihradi.ebankbackend.dtos.SavingBankAccountDTO;
import ma.mihradi.ebankbackend.entities.CurrentAccount;
import ma.mihradi.ebankbackend.entities.Customer;
import ma.mihradi.ebankbackend.entities.SavingAccount;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
//MapStruct
@Service
public class BankAccountMapperImpl {
    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO=new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName (customer.getName());
        customerDTO.setEmail(customer.getEmail());
        return customerDTO;
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer=new Customer();
        BeanUtils.copyProperties (customerDTO,customer);
        return customer;
    }

    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount){
        SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingAccount,savingBankAccountDTO);
        savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        return savingBankAccountDTO;
    }
    public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO){

        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDTO,savingAccount);
        savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
        return savingAccount;
    }
    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount){
        CurrentBankAccountDTO currentBankAccountDTO=new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentAccount,currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDTO(fromCustomer (currentAccount.getCustomer ()));
        return currentBankAccountDTO;
    }
    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO){
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO,currentAccount);
        currentAccount.setCustomer (fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
        return currentAccount;
    }
}

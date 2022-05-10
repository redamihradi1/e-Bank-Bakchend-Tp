package ma.mihradi.ebankbackend.services;

import ma.mihradi.ebankbackend.dtos.CustomerDTO;
import ma.mihradi.ebankbackend.entities.BankAccount;
import ma.mihradi.ebankbackend.entities.CurrentAccount;
import ma.mihradi.ebankbackend.entities.Customer;
import ma.mihradi.ebankbackend.entities.SavingAccount;
import ma.mihradi.ebankbackend.exceptions.BalanceNotSufficentException;
import ma.mihradi.ebankbackend.exceptions.BankAccountNotFoundException;
import ma.mihradi.ebankbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    public Customer saveCustomer(Customer customer);
    CurrentAccount saveCurrentBankAccount(double initialBalance , double overDraft , Long costumerId) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initialBalance , double interestRate , Long costumerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer (String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficentException;
    List<BankAccount> bankAccountList();
}
package ma.mihradi.ebankbackend.services;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.mihradi.ebankbackend.dtos.*;
import ma.mihradi.ebankbackend.entities.*;
import ma.mihradi.ebankbackend.enums.OperationType;
import ma.mihradi.ebankbackend.exceptions.BalanceNotSufficentException;
import ma.mihradi.ebankbackend.exceptions.BankAccountNotFoundException;
import ma.mihradi.ebankbackend.exceptions.CustomerNotFoundException;
import ma.mihradi.ebankbackend.mappers.BankAccountMapperImpl;
import ma.mihradi.ebankbackend.repositories.AccountOperationRepository;
import ma.mihradi.ebankbackend.repositories.BankAccountRepository;
import ma.mihradi.ebankbackend.repositories.CustomerRepository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor // injection de déependances
@Slf4j
public class BankAccountServiceImpl implements  BankAccountService{

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    //****   Slf4j    *****     Logger log = LoggerFactory.getLogger(this.getClass().getName());


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving a new Customer...");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer =  customerRepository.save (customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long costumerId) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(costumerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);

        CurrentAccount savedCurrentAccount = bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentBankAccount(savedCurrentAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long costumerId) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(costumerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);

        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingBankAccount(savedBankAccount);
    }


    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers=customerRepository.findAll();

        List<CustomerDTO> customerDTOS=new ArrayList<>();
        for (Customer customer:customers){
            CustomerDTO customerDTO=dtoMapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        }
        return customerDTOS;

    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(
                ()-> new BankAccountNotFoundException("Bank account not found"));
        if (bankAccount instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        }else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);
        }

    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException {
            BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(
                ()-> new BankAccountNotFoundException("Bank account not found"));
            if(bankAccount.getBalance()<amount) throw new BalanceNotSufficentException("Balance not sufficent");
            AccountOperation accountOperation= new AccountOperation();
            accountOperation.setType (OperationType.DEBIT);
            accountOperation.setAmount (amount);
            accountOperation.setDescription(description);
            accountOperation.setOperationDate(new Date());
            accountOperation.setBankAccount(bankAccount);
            accountOperationRepository.save(accountOperation);
            bankAccount.setBalance(bankAccount.getBalance()-amount);
            bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(
                ()-> new BankAccountNotFoundException("Bank account not found"));

        AccountOperation accountOperation= new AccountOperation();
        accountOperation.setType (OperationType.CREDIT);
        accountOperation.setAmount (amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficentException {
        debit(accountIdSource,amount,"Transfer to"+accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from"+accountIdSource);

    }

    @Override
    public List<BankAccountDTO> bankAccountList(){
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if(bankAccount instanceof SavingAccount){
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavingBankAccount(savingAccount);
            }
            else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());

        return bankAccountDTOS;
    }


    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException{
        Customer customer=customerRepository.findById (customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer Not found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving a new Customer...");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer =  customerRepository.save (customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        final List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return  accountOperations.stream().map(accountOperation -> dtoMapper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDto getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null) throw  new BankAccountNotFoundException("Account not found");
        final Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page, size));
        AccountHistoryDto accountHistoryDto =new AccountHistoryDto();
        final List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(accountOperation -> dtoMapper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
        accountHistoryDto.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDto.setAccountId(bankAccount.getId());
        accountHistoryDto.setBalance(bankAccount.getBalance());
        accountHistoryDto.setSize(size);
        accountHistoryDto.setCurrentPage(page);
        accountHistoryDto.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDto;
    }

}

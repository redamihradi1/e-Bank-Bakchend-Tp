package ma.mihradi.ebankbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.mihradi.ebankbackend.dtos.CustomerDTO;
import ma.mihradi.ebankbackend.entities.Customer;
import ma.mihradi.ebankbackend.exceptions.CustomerNotFoundException;
import ma.mihradi.ebankbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<CustomerDTO> customers (){
        return bankAccountService.listCustomers();
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
        return  bankAccountService.getCustomer(customerId);
    }

    @PostMapping ("/customers")
    public CustomerDTO saveCustomer (@RequestBody CustomerDTO customerDTO) {
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("/customers/{customerid}")
    public CustomerDTO updateCustomer(@PathVariable Long customerid,@RequestBody CustomerDTO customerDTO){
        customerDTO.setId(customerid);
        return  bankAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customers/{customerid}")
    public  void deleteCustomer(@PathVariable Long customerid){
     bankAccountService.deleteCustomer(customerid);
    }

}

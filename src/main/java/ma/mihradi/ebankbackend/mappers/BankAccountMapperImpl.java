package ma.mihradi.ebankbackend.mappers;

import lombok.extern.slf4j.Slf4j;
import ma.mihradi.ebankbackend.dtos.CustomerDTO;
import ma.mihradi.ebankbackend.entities.Customer;
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
}

package ma.mihradi.ebankbackend.dtos;

import lombok.Data;
import ma.mihradi.ebankbackend.entities.AccountOperationDTO;

import java.util.List;

@Data
public class AccountHistoryDto {

    private String accountId;
    private double balance;
    private int currentPage;
    private int totalPages;
    private int size;
    private List<AccountOperationDTO> accountOperationDTOS;
}

package ru.springtest.mapper;

import org.springframework.stereotype.Component;
import ru.springtest.domain.Accounts;
import ru.springtest.domain.Customers;
import ru.springtest.dto.CustomerAccountResponseDto;

@Component
public class CustomerAccountMapper {
    public CustomerAccountResponseDto toDto (Customers customers, String accountData) {
        return new CustomerAccountResponseDto(
                customers.getId(),
                customers.getCustomerName(),
                accountData
        );
    }
}

package ru.springtest.service;

import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.Accounts;
import ru.springtest.domain.Customers;
import ru.springtest.dto.CustomerAccountCreateUpdateDto;
import ru.springtest.dto.CustomerAccountResponseDto;

import java.util.UUID;

public interface CustomersAccountsService {
    CustomerAccountResponseDto createWithAccount(CustomerAccountCreateUpdateDto dto);
    CustomerAccountResponseDto getById(UUID id);
    CustomerAccountResponseDto updateWithAccount(CustomerAccountCreateUpdateDto dto);
    void delete(UUID id);
}
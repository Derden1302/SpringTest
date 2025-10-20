package ru.springtest.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.Accounts;
import ru.springtest.domain.Customers;
import ru.springtest.dto.CustomerAccountCreateUpdateDto;
import ru.springtest.dto.CustomerAccountResponseDto;
import ru.springtest.mapper.CustomerAccountMapper;
import ru.springtest.repository.AccountsRepository;
import ru.springtest.repository.CustomersRepository;
import ru.springtest.service.CustomersAccountsService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomersAccountsServiceImplementation implements CustomersAccountsService {
    private final CustomersRepository customersRepository;
    private final AccountsRepository accountsRepository;
    private final CustomerAccountMapper mapper;

    @Transactional
    @Override
    public CustomerAccountResponseDto createWithAccount(CustomerAccountCreateUpdateDto dto) {
        Customers customers = customersRepository.save(mapper.createCustomer(dto));
        Accounts accounts = accountsRepository.save(mapper.createAccount(dto, customers));
        return mapper.toDto(customers, accounts);
    }

    @Transactional
    @Override
    public CustomerAccountResponseDto getById(UUID id) {
        Customers customer = customersRepository.findById(id).orElse(null);
        return mapper.toDto(
                customer,
                accountsRepository.findAccountsByCustomerId(id));
    }

    @Override
    @Transactional
    public CustomerAccountResponseDto updateWithAccount(CustomerAccountCreateUpdateDto dto) {
        Customers customer = customersRepository.findByName(dto.name());
        mapper.updateCustomer(customer, dto);
        customersRepository.save(customer);
        Accounts accounts = accountsRepository.findAccountsByCustomerId(customer.getId());
        mapper.updateAccount(accounts, dto);
        accountsRepository.save(accounts);
        return mapper.toDto(customer,accounts);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        customersRepository.deleteById(id);
    }

}

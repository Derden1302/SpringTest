package ru.springtest.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.Account;
import ru.springtest.domain.Customer;
import ru.springtest.dto.CustomerAccountCreateUpdateDto;
import ru.springtest.dto.CustomerAccountResponseDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.mapper.CustomerAccountMapper;
import ru.springtest.repository.AccountRepository;
import ru.springtest.repository.CustomerRepository;
import ru.springtest.service.CustomersAccountsService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomersAccountsServiceImplementation implements CustomersAccountsService {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final CustomerAccountMapper mapper;

    @Transactional
    @Override
    public CustomerAccountResponseDto createWithAccount(CustomerAccountCreateUpdateDto dto) {
        Customer customer = customerRepository.save(mapper.toEntity(dto));
        Account account = accountRepository.save(mapper.toEntity(dto, customer));
        return mapper.toDto(customer, account);
    }

    @Transactional
    @Override
    public CustomerAccountResponseDto getById(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow(()->new NotFoundException("Customer not found with id:" + id));
        return mapper.toDto(
                customer,
                accountRepository.findAccountsByCustomerId(id));
    }

    @Override
    @Transactional
    public CustomerAccountResponseDto updateWithAccount(CustomerAccountCreateUpdateDto dto, UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow(()->new NotFoundException("Customer not found with id:" + id));
        mapper.changeCustomer(customer, dto);
        customerRepository.save(customer);
        Account account = accountRepository.findAccountsByCustomerId(id);
        mapper.changeAccount(account, dto);
        accountRepository.save(account);
        return mapper.toDto(customer, account);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        customerRepository.deleteById(id);
    }

}

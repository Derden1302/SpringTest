package ru.springtest.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
import ru.springtest.service.CustomerAccountService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerAccountServiceImplementation implements CustomerAccountService {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final CustomerAccountMapper mapper;

    @CachePut(value = "customer", key = "#result.customerId()")
    @Transactional
    @Override
    public CustomerAccountResponseDto createWithAccount(CustomerAccountCreateUpdateDto dto) {
        Customer customer = customerRepository.save(mapper.toEntity(dto));
        Account account = accountRepository.save(mapper.toEntity(dto, customer));
        return mapper.toDto(customer, account);
    }

    @Cacheable(value = "customer", key = "#id")
    @Transactional
    @Override
    public CustomerAccountResponseDto getById(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow(()->new NotFoundException("Customer not found with id: " + id));
        return mapper.toDto(
                customer,
                accountRepository.findAccountsByCustomerId(id));
    }

    @CachePut(value = "customer", key = "#result.customerId()")
    @Override
    @Transactional
    public CustomerAccountResponseDto updateWithAccount(CustomerAccountCreateUpdateDto dto, UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow(()->new NotFoundException("Customer not found with id: " + id));
        mapper.changeCustomer(customer, dto);
        customerRepository.save(customer);
        Account account = accountRepository.findAccountsByCustomerId(id);
        mapper.changeAccount(account, dto);
        accountRepository.save(account);
        return mapper.toDto(customer, account);
    }

    @CacheEvict(value = "customer", key = "#id")
    @Override
    @Transactional
    public void delete(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new NotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

}

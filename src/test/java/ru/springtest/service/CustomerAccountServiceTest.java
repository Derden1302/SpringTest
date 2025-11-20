package ru.springtest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.MockitoAnnotations;
import ru.springtest.domain.Account;
import ru.springtest.domain.Customer;
import ru.springtest.dto.CustomerAccountCreateUpdateDto;
import ru.springtest.dto.CustomerAccountResponseDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.mapper.CustomerAccountMapper;
import ru.springtest.repository.AccountRepository;
import ru.springtest.repository.CustomerRepository;
import ru.springtest.service.implementation.CustomerAccountServiceImplementation;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CustomerAccountServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CustomerAccountMapper mapper;
    @InjectMocks
    private CustomerAccountServiceImplementation service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createWithAccount_success() {
        UUID id = UUID.randomUUID();
        CustomerAccountCreateUpdateDto dto = new CustomerAccountCreateUpdateDto(
                "TestCustomer",
                "TestAccount"
        );
        Customer customer = new  Customer();
        customer.setId(id);
        Account account = new  Account();
        CustomerAccountResponseDto responseDto = new CustomerAccountResponseDto(
                id, "TestCustomer", "TestAccount"
        );
        when(mapper.toEntity(dto)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(mapper.toEntity(dto,customer)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        when(mapper.toDto(customer, account)).thenReturn(responseDto);
        CustomerAccountResponseDto result = service.createWithAccount(dto);
        assertThat(result.customerId()).isEqualTo(id);
    }

    @Test
    void updateWithAccount_success() {
        UUID id = UUID.randomUUID();
        CustomerAccountCreateUpdateDto dto = new CustomerAccountCreateUpdateDto(
                "TestCustomer",
                "TestAccount"
        );
        Customer customer = new  Customer();
        customer.setId(id);
        Account account = new  Account();
        CustomerAccountResponseDto responseDto = new CustomerAccountResponseDto(
                id, "TestCustomer", "TestAccount"
        );
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);
        when(mapper.toDto(customer, account)).thenReturn(responseDto);
        when(accountRepository.findAccountsByCustomerId(id)).thenReturn(account);
        CustomerAccountResponseDto result = service.updateWithAccount(dto, id);
        assertThat(result.customerId()).isEqualTo(id);
        verify(mapper).changeAccount(account, dto);
        verify(mapper).changeCustomer(customer, dto);
    }

    @Test
    void updateWithAccount_notFound() {
        UUID id = UUID.randomUUID();
        CustomerAccountCreateUpdateDto dto = new CustomerAccountCreateUpdateDto(
                "TestCustomer",
                "TestAccount"
        );
        when(customerRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.updateWithAccount(dto, id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Customer not found with id: " + id);
        verify(customerRepository, never()).save(any());
    }

    @Test
    void getById_success() {
        UUID id = UUID.randomUUID();
        Customer customer = new  Customer();
        customer.setId(id);
        Account account = new  Account();
        CustomerAccountResponseDto responseDto = new CustomerAccountResponseDto(
                id, "TestCustomer", "TestAccount"
        );
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(accountRepository.findAccountsByCustomerId(id)).thenReturn(account);
        when(mapper.toDto(customer, account)).thenReturn(responseDto);
        CustomerAccountResponseDto result = service.getById(id);
        assertThat(result.customerId()).isEqualTo(id);
    }

    @Test
    void getById_notFound() {
        UUID id = UUID.randomUUID();
        when(customerRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Customer not found with id: " + id);
    }

    @Test
    void delete_success() {
        UUID id = UUID.randomUUID();
        when(customerRepository.existsById(id)).thenReturn(true);
        service.delete(id);
        verify(customerRepository).deleteById(id);
    }

    @Test
    void delete_notFound() {
        UUID id = UUID.randomUUID();
        Customer customer = new  Customer();
        when(customerRepository.existsById(id)).thenReturn(false);
        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Customer not found with id: " + id);
        verify(customerRepository, never()).deleteById(id);
    }

}

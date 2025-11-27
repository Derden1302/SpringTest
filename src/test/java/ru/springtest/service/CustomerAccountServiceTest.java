package ru.springtest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.springtest.domain.Account;
import ru.springtest.domain.Customer;
import ru.springtest.dto.CustomerAccountCreateUpdateDto;
import ru.springtest.dto.CustomerAccountResponseDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.repository.AccountRepository;
import ru.springtest.repository.CustomerRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CustomerAccountServiceTest extends AbstractIntegrationTest{

    @Autowired
    CustomerAccountService service;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AccountRepository accountRepository;

    static final UUID CUSTOMER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    static final String CUSTOMER_NAME = "TestCustomer";
    static final String ACCOUNT_NAME = "TestAccount";

    @Test
    @Transactional
    void createWithAccount_success() {
        CustomerAccountCreateUpdateDto dto = new CustomerAccountCreateUpdateDto(
                CUSTOMER_NAME,
                ACCOUNT_NAME
        );
        CustomerAccountResponseDto result = service.createWithAccount(dto);
        assertThat(result.customerId()).isNotNull();
        Customer savedCustomer = customerRepository.findById(result.customerId()).orElseThrow();
        assertThat(savedCustomer.getName()).isEqualTo(CUSTOMER_NAME);
        Account savedAccount = accountRepository.findAccountsByCustomerId(result.customerId());
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getAccountData()).isEqualTo(ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void updateWithAccount_success() {
        CustomerAccountCreateUpdateDto createDto = new CustomerAccountCreateUpdateDto(
                CUSTOMER_NAME,
                ACCOUNT_NAME
        );
        CustomerAccountResponseDto created = service.createWithAccount(createDto);
        CustomerAccountCreateUpdateDto updateDto = new CustomerAccountCreateUpdateDto(
                "UpdatedCustomer",
                "UpdatedAccount"
        );
        CustomerAccountResponseDto result = service.updateWithAccount(updateDto, created.customerId());
        assertThat(result.customerId()).isEqualTo(created.customerId());
        Customer updatedCustomer = customerRepository.findById(created.customerId()).orElseThrow();
        assertThat(updatedCustomer.getName()).isEqualTo("UpdatedCustomer");
        Account updatedAccount = accountRepository.findAccountsByCustomerId(created.customerId());
        assertThat(updatedAccount.getAccountData()).isEqualTo("UpdatedAccount");
    }

    @Test
    @Transactional
    void updateWithAccount_notFound() {
        CustomerAccountCreateUpdateDto dto = new CustomerAccountCreateUpdateDto(
                CUSTOMER_NAME,
                ACCOUNT_NAME
        );
        assertThatThrownBy(() -> service.updateWithAccount(dto, CUSTOMER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Customer not found with id: " + CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getById_success() {
        CustomerAccountCreateUpdateDto dto = new CustomerAccountCreateUpdateDto(
                CUSTOMER_NAME,
                ACCOUNT_NAME
        );
        CustomerAccountResponseDto created = service.createWithAccount(dto);
        CustomerAccountResponseDto result = service.getById(created.customerId());
        assertThat(result.customerId()).isEqualTo(created.customerId());
    }

    @Test
    @Transactional
    void getById_notFound() {
        assertThatThrownBy(() -> service.getById(CUSTOMER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Customer not found with id: " + CUSTOMER_ID);
    }

    @Test
    @Transactional
    void delete_success() {
        CustomerAccountCreateUpdateDto dto = new CustomerAccountCreateUpdateDto(
                CUSTOMER_NAME,
                ACCOUNT_NAME);
        CustomerAccountResponseDto created = service.createWithAccount(dto);
        service.delete(created.customerId());
        assertThat(customerRepository.findById(created.customerId())).isEmpty();
    }

    @Test
    @Transactional
    void delete_notFound() {
        assertThatThrownBy(() -> service.delete(CUSTOMER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Customer not found with id: " + CUSTOMER_ID);
    }
}

package ru.springtest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.springtest.domain.Account;
import ru.springtest.domain.Customer;
import ru.springtest.dto.CustomerAccountCreateUpdateDto;
import ru.springtest.dto.CustomerAccountResponseDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.repository.AccountRepository;
import ru.springtest.repository.CustomerRepository;
import ru.springtest.service.implementation.CustomerAccountServiceImplementation;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@SpringBootTest
class CustomerAccountServiceTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14");
    @ServiceConnection(name = "redis")
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:8.2.3"))
            .withExposedPorts(6379);

    @Autowired
    CustomerAccountServiceImplementation service;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AccountRepository accountRepository;

    @Test
    @Transactional
    void createWithAccount_success() {
        CustomerAccountCreateUpdateDto dto = new CustomerAccountCreateUpdateDto(
                "TestCustomer",
                "TestAccount"
        );
        CustomerAccountResponseDto result = service.createWithAccount(dto);
        assertThat(result.customerId()).isNotNull();
        Customer savedCustomer = customerRepository.findById(result.customerId()).orElseThrow();
        assertThat(savedCustomer.getName()).isEqualTo("TestCustomer");
        Account savedAccount = accountRepository.findAccountsByCustomerId(result.customerId());
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getAccountData()).isEqualTo("TestAccount");
    }

    @Test
    @Transactional
    void updateWithAccount_success() {
        CustomerAccountCreateUpdateDto createDto = new CustomerAccountCreateUpdateDto(
                "OldCustomer",
                "OldAccount"
        );
        CustomerAccountResponseDto created = service.createWithAccount(createDto);
        UUID id = created.customerId();
        CustomerAccountCreateUpdateDto updateDto = new CustomerAccountCreateUpdateDto(
                "UpdatedCustomer",
                "UpdatedAccount"
        );
        CustomerAccountResponseDto result = service.updateWithAccount(updateDto, id);
        assertThat(result.customerId()).isEqualTo(id);
        Customer updatedCustomer = customerRepository.findById(id).orElseThrow();
        assertThat(updatedCustomer.getName()).isEqualTo("UpdatedCustomer");
        Account updatedAccount = accountRepository.findAccountsByCustomerId(id);
        assertThat(updatedAccount.getAccountData()).isEqualTo("UpdatedAccount");
    }

    @Test
    @Transactional
    void updateWithAccount_notFound() {
        UUID id = UUID.randomUUID();
        CustomerAccountCreateUpdateDto dto = new CustomerAccountCreateUpdateDto(
                "TestCustomer",
                "TestAccount"
        );
        assertThatThrownBy(() -> service.updateWithAccount(dto, id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Customer not found with id: " + id);
    }

    @Test
    @Transactional
    void getById_success() {
        CustomerAccountCreateUpdateDto dto = new CustomerAccountCreateUpdateDto(
                "TestCustomer",
                "TestAccount"
        );
        CustomerAccountResponseDto created = service.createWithAccount(dto);
        UUID id = created.customerId();
        CustomerAccountResponseDto result = service.getById(id);
        assertThat(result.customerId()).isEqualTo(id);
    }

    @Test
    @Transactional
    void getById_notFound() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> service.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Customer not found with id: " + id);
    }

    @Test
    @Transactional
    void delete_success() {
        CustomerAccountCreateUpdateDto dto = new CustomerAccountCreateUpdateDto(
                "ToDelete",
                "Account");
        CustomerAccountResponseDto created = service.createWithAccount(dto);
        UUID id = created.customerId();
        service.delete(id);
        assertThat(customerRepository.findById(id)).isEmpty();
    }

    @Test
    @Transactional
    void delete_notFound() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Customer not found with id: " + id);
    }
}

package ru.springtest.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.springtest.domain.Contract;
import ru.springtest.domain.History;
import ru.springtest.dto.ContractCreateUpdateDto;
import ru.springtest.dto.ContractResponseDto;
import ru.springtest.dto.HistoryDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.mapper.ContractMapper;
import ru.springtest.mapper.HistoryMapper;
import ru.springtest.repository.ContractRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.springtest.service.implementation.ContractServiceImplementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Testcontainers
@SpringBootTest
class ContractServiceTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14");
    @ServiceConnection(name = "redis")
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:8.2.3"))
            .withExposedPorts(6379);

    @Autowired
    ContractService service;
    @Autowired
    ContractRepository contractRepository;


    @Test
    @Transactional
        // Откатываем транзакцию после теста, чтобы база была чистой
    void createContract_success() {
        ContractCreateUpdateDto dto = new ContractCreateUpdateDto(
                "TestContract",
                List.of(new HistoryDto("TestHistory"))
        );
        ContractResponseDto result = service.createContract(dto);
        assertThat(result.id()).isNotNull();
        assertThat(result.name()).isEqualTo("TestContract");
        assertThat(result.history()).hasSize(1);

        Contract savedContract = contractRepository.findById(result.id()).orElseThrow();
        assertThat(savedContract.getName()).isEqualTo("TestContract");
        assertThat(savedContract.getHistory()).hasSize(1);

    }

    @Test
    @Transactional
    void updateContract_success() {
        ContractCreateUpdateDto dto = new ContractCreateUpdateDto(
                "TestContract",
                List.of(new HistoryDto("TestHistory"))
        );
        ContractResponseDto createdContract = service.createContract(dto);
        UUID id = createdContract.id();
        ContractCreateUpdateDto updateDto = new ContractCreateUpdateDto(
                "UpdatedContract",
                List.of(new HistoryDto("TestHistory")));
        ContractResponseDto result = service.updateContract(id, updateDto);
        assertThat(result.name()).isEqualTo("UpdatedContract");
        Contract updatedEntity = contractRepository.findById(id).orElseThrow();
        assertThat(updatedEntity.getName()).isEqualTo("UpdatedContract");
        assertThat(updatedEntity.getHistory()).hasSize(1);
    }

    @Test
    @Transactional
    void updateContract_notFound() {
        UUID id = UUID.randomUUID();
        ContractCreateUpdateDto dto = new ContractCreateUpdateDto(
                "TestContract", List.of(new HistoryDto("TestHistory"))
        );
        assertThatThrownBy(() -> service.updateContract(id, dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Contract not found with id: " + id);
    }

    @Test
    @Transactional
    void deleteContract_success() {
        ContractCreateUpdateDto dto = new ContractCreateUpdateDto(
                "TestContract",
                List.of(new HistoryDto("TestHistory"))
        );
        ContractResponseDto createdContract = service.createContract(dto);
        UUID id = createdContract.id();
        service.deleteContract(id);
        assertThat(contractRepository.findById(id)).isEmpty();
    }

    @Test
    @Transactional
    void deleteContract_notFound() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> service.deleteContract(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Contract not found with id: " + id);
    }

    @Test
    @Transactional
    void getContract_success() {
        ContractCreateUpdateDto dto = new ContractCreateUpdateDto(
                "TestContract",
                List.of(new HistoryDto("TestHistory"))
        );
        ContractResponseDto createdContract = service.createContract(dto);
        UUID id = createdContract.id();
        ContractResponseDto result = service.getContract(id);
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.name()).isEqualTo("TestContract");
    }

    @Test
    @Transactional
    void getContract_notFound() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> service.getContract(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Contract not found with id: " + id);
    }
}
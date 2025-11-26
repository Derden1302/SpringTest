package ru.springtest.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.springtest.domain.Contract;
import ru.springtest.dto.ContractCreateUpdateDto;
import ru.springtest.dto.ContractResponseDto;
import ru.springtest.dto.HistoryDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.repository.ContractRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@SpringBootTest
class ContractServiceTest extends AbstractIntegrationTest{

    @Autowired
    ContractService service;
    @Autowired
    ContractRepository contractRepository;

    static final UUID CONTRACT_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    static final String CONTRACT_NAME = "TestContract";
    static final String HISTORY_NAME = "TestHistory";


    @Test
    @Transactional
    void createContract_success() {
        ContractCreateUpdateDto dto = new ContractCreateUpdateDto(
                CONTRACT_NAME,
                List.of(new HistoryDto(HISTORY_NAME))
        );
        ContractResponseDto result = service.createContract(dto);
        assertThat(result.id()).isNotNull();
        assertThat(result.name()).isEqualTo(CONTRACT_NAME);
        assertThat(result.history()).hasSize(1);

        Contract savedContract = contractRepository.findById(result.id()).orElseThrow();
        assertThat(savedContract.getName()).isEqualTo(CONTRACT_NAME);
        assertThat(savedContract.getHistory()).hasSize(1);

    }

    @Test
    @Transactional
    void updateContract_success() {
        ContractCreateUpdateDto dto = new ContractCreateUpdateDto(
                CONTRACT_NAME,
                List.of(new HistoryDto(HISTORY_NAME))
        );
        ContractResponseDto created = service.createContract(dto);
        ContractCreateUpdateDto updateDto = new ContractCreateUpdateDto(
                "UpdatedContract",
                List.of(new HistoryDto(HISTORY_NAME)));
        ContractResponseDto result = service.updateContract(created.id(), updateDto);
        assertThat(result.name()).isEqualTo("UpdatedContract");
        Contract updatedEntity = contractRepository.findById(created.id()).orElseThrow();
        assertThat(updatedEntity.getName()).isEqualTo("UpdatedContract");
        assertThat(updatedEntity.getHistory()).hasSize(1);
    }

    @Test
    @Transactional
    void updateContract_notFound() {
        ContractCreateUpdateDto dto = new ContractCreateUpdateDto(
                CONTRACT_NAME, List.of(new HistoryDto(HISTORY_NAME))
        );
        assertThatThrownBy(() -> service.updateContract(CONTRACT_ID, dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Contract not found with id: " + CONTRACT_ID);
    }

    @Test
    @Transactional
    void deleteContract_success() {
        ContractCreateUpdateDto dto = new ContractCreateUpdateDto(
                CONTRACT_NAME,
                List.of(new HistoryDto(HISTORY_NAME))
        );
        ContractResponseDto created = service.createContract(dto);
        service.deleteContract(created.id());
        assertThat(contractRepository.findById(created.id())).isEmpty();
    }

    @Test
    @Transactional
    void deleteContract_notFound() {
        assertThatThrownBy(() -> service.deleteContract(CONTRACT_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Contract not found with id: " + CONTRACT_ID);
    }

    @Test
    @Transactional
    void getContract_success() {
        ContractCreateUpdateDto dto = new ContractCreateUpdateDto(
                CONTRACT_NAME,
                List.of(new HistoryDto(HISTORY_NAME))
        );
        ContractResponseDto created = service.createContract(dto);
        ContractResponseDto result = service.getContract(created.id());
        assertThat(result.id()).isEqualTo(created.id());
        assertThat(result.name()).isEqualTo(CONTRACT_NAME);
    }

    @Test
    @Transactional
    void getContract_notFound() {
        assertThatThrownBy(() -> service.getContract(CONTRACT_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Contract not found with id: " + CONTRACT_ID);
    }
}
package ru.springtest.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.springtest.domain.History;
import ru.springtest.dto.ContractDto;
import ru.springtest.dto.HistoryCreateUpdateDto;
import ru.springtest.dto.HistoryResponseDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.repository.HistoryRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HistoryServiceTest extends AbstractIntegrationTest{

    @Autowired
    HistoryService service;
    @Autowired
    HistoryRepository historyRepository;
    
    static final UUID HISTORY_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
    static final String CONTRACT_NAME = "TestContract";
    static final String HISTORY_NAME = "TestHistory";

    @Test
    @Transactional
    void createHistory_success() {
        HistoryCreateUpdateDto dto = new HistoryCreateUpdateDto(
                HISTORY_NAME,
                List.of(new ContractDto(CONTRACT_NAME))
        );
        HistoryResponseDto result = service.createHistory(dto);
        assertThat(result.id()).isNotNull();
        assertThat(result.name()).isEqualTo(HISTORY_NAME);
        assertThat(result.contract()).hasSize(1);

        History savedHistory = historyRepository.findById(result.id()).orElseThrow();
        assertThat(savedHistory.getName()).isEqualTo(HISTORY_NAME);
        assertThat(savedHistory.getContract()).hasSize(1);
    }

    @Test
    @Transactional
    void updateHistory_success() {
        HistoryCreateUpdateDto dto = new HistoryCreateUpdateDto(
                HISTORY_NAME,
                List.of(new ContractDto(CONTRACT_NAME))
        );
        HistoryResponseDto created = service.createHistory(dto);
        HistoryCreateUpdateDto updateDto = new HistoryCreateUpdateDto(
                "UpdatedHistory",
                List.of(new ContractDto(CONTRACT_NAME)));
        HistoryResponseDto result = service.updateHistory(created.id(), updateDto);
        assertThat(result.name()).isEqualTo("UpdatedHistory");
        History updatedEntity = historyRepository.findById(created.id()).orElseThrow();
        assertThat(updatedEntity.getName()).isEqualTo("UpdatedHistory");
        assertThat(updatedEntity.getContract()).hasSize(1);
    }

    @Test
    @Transactional
    void updateHistory_notFound() {
        HistoryCreateUpdateDto dto = new HistoryCreateUpdateDto(
                HISTORY_NAME, List.of(new ContractDto(CONTRACT_NAME))
        );
        assertThatThrownBy(() -> service.updateHistory(HISTORY_ID, dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("History not found with id: " + HISTORY_ID);
    }

    @Test
    @Transactional
    void deleteHistory_success() {
        HistoryCreateUpdateDto dto = new HistoryCreateUpdateDto(
                HISTORY_NAME,
                List.of(new ContractDto(CONTRACT_NAME))
        );
        HistoryResponseDto created = service.createHistory(dto);
        service.deleteHistory(created.id());
        assertThat(historyRepository.findById(created.id())).isEmpty();
    }

    @Test
    @Transactional
    void deleteHistory_notFound() {
        assertThatThrownBy(() -> service.deleteHistory(HISTORY_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("History not found with id: " + HISTORY_ID);
    }

    @Test
    @Transactional
    void getHistory_success() {
        HistoryCreateUpdateDto dto = new HistoryCreateUpdateDto(
                HISTORY_NAME,
                List.of(new ContractDto(CONTRACT_NAME))
        );
        HistoryResponseDto created = service.createHistory(dto);
        HistoryResponseDto result = service.getHistory(created.id());
        assertThat(result.id()).isEqualTo(created.id());
        assertThat(result.name()).isEqualTo(HISTORY_NAME);
    }

    @Test
    @Transactional
    void getHistory_notFound() {
        assertThatThrownBy(() -> service.getHistory(HISTORY_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("History not found with id: " + HISTORY_ID);
    }

}

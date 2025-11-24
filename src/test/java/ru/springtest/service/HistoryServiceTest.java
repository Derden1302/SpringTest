package ru.springtest.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.springtest.domain.Contract;
import ru.springtest.domain.History;
import ru.springtest.dto.*;
import ru.springtest.exception.NotFoundException;
import ru.springtest.repository.HistoryRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@SpringBootTest
public class HistoryServiceTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14");
    @ServiceConnection(name = "redis")
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:8.2.3"))
            .withExposedPorts(6379);

    @Autowired
    HistoryService service;
    @Autowired
    HistoryRepository historyRepository;

    @Test
    @Transactional

    void createHistory_success() {
        HistoryCreateUpdateDto dto = new HistoryCreateUpdateDto(
                "TestHistory",
                List.of(new ContractDto("TestContract"))
        );
        HistoryResponseDto result = service.createHistory(dto);
        assertThat(result.id()).isNotNull();
        assertThat(result.name()).isEqualTo("TestHistory");
        assertThat(result.contract()).hasSize(1);

        History savedHistory = historyRepository.findById(result.id()).orElseThrow();
        assertThat(savedHistory.getName()).isEqualTo("TestHistory");
        assertThat(savedHistory.getContract()).hasSize(1);
    }

    @Test
    @Transactional
    void updateHistory_success() {
        HistoryCreateUpdateDto dto = new HistoryCreateUpdateDto(
                "TestHistory",
                List.of(new ContractDto("TestContract"))
        );
        HistoryResponseDto createdHistory = service.createHistory(dto);
        UUID id = createdHistory.id();
        HistoryCreateUpdateDto updateDto = new HistoryCreateUpdateDto(
                "UpdatedHistory",
                List.of(new ContractDto("TestContract")));
        HistoryResponseDto result = service.updateHistory(id, updateDto);
        assertThat(result.name()).isEqualTo("UpdatedHistory");
        History updatedEntity = historyRepository.findById(id).orElseThrow();
        assertThat(updatedEntity.getName()).isEqualTo("UpdatedHistory");
        assertThat(updatedEntity.getContract()).hasSize(1);
    }

    @Test
    @Transactional
    void updateHistory_notFound() {
        UUID id = UUID.randomUUID();
        HistoryCreateUpdateDto dto = new HistoryCreateUpdateDto(
                "TestHistory", List.of(new ContractDto("TestContract"))
        );
        assertThatThrownBy(() -> service.updateHistory(id, dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("History not found with id: " + id);
    }

    @Test
    @Transactional
    void deleteHistory_success() {
        HistoryCreateUpdateDto dto = new HistoryCreateUpdateDto(
                "TestHistory",
                List.of(new ContractDto("TestContract"))
        );
        HistoryResponseDto createdHistory = service.createHistory(dto);
        UUID id = createdHistory.id();
        service.deleteHistory(id);
        assertThat(historyRepository.findById(id)).isEmpty();
    }

    @Test
    @Transactional
    void deleteHistory_notFound() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> service.deleteHistory(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("History not found with id: " + id);
    }

    @Test
    @Transactional
    void getHistory_success() {
        HistoryCreateUpdateDto dto = new HistoryCreateUpdateDto(
                "TestHistory",
                List.of(new ContractDto("TestContract"))
        );
        HistoryResponseDto createdHistory = service.createHistory(dto);
        UUID id = createdHistory.id();
        HistoryResponseDto result = service.getHistory(id);
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.name()).isEqualTo("TestHistory");
    }

    @Test
    @Transactional
    void getHistory_notFound() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> service.getHistory(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("History not found with id: " + id);
    }

}

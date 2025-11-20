package ru.springtest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.springtest.domain.Contract;
import ru.springtest.domain.History;
import ru.springtest.dto.*;
import ru.springtest.exception.NotFoundException;
import ru.springtest.mapper.ContractMapper;
import ru.springtest.mapper.HistoryMapper;
import ru.springtest.repository.ContractRepository;
import ru.springtest.repository.HistoryRepository;
import ru.springtest.service.implementation.ContractServiceImplementation;
import ru.springtest.service.implementation.HistoryServiceImplementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HistoryServiceTest {
    @Mock
    private HistoryRepository historyRepository;
    @Mock
    private ContractMapper contractMapper;
    @Mock
    private HistoryMapper historyMapper;
    @InjectMocks
    private HistoryServiceImplementation service;
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createHistory_success()
    {
        UUID id = UUID.randomUUID();
        HistoryCreateUpdateDto dto = new HistoryCreateUpdateDto(
                "TestHistory",
                List.of(new ContractDto("TestContract"))
        );
        History history = new History();
        history.setId(id);
        Contract contract = new Contract();
        HistoryResponseDto responseDto = new HistoryResponseDto(
                id, "TestHistory", List.of(new ContractDto("TestContract")));
        when(historyMapper.toEntity(dto)).thenReturn(history);
        when(contractMapper.toEntityListContract(dto.contractsDtos())).thenReturn(List.of(contract));
        when(historyRepository.save(history)).thenReturn(history);
        when(historyMapper.toResponseDto(history)).thenReturn(responseDto);
        HistoryResponseDto result = service.createHistory(dto);
        assertThat(result.id()).isEqualTo(id);
        verify(historyMapper).changeHistory(history,List.of(contract));
    }

    @Test
    void updateHistory_success(){
        UUID id = UUID.randomUUID();
        HistoryCreateUpdateDto dto = new HistoryCreateUpdateDto(
                "TestHistory",
                List.of(new ContractDto("TestContract"))
        );
        History existing = new History();
        existing.setId(id);
        Contract contract = new Contract();
        HistoryResponseDto responseDto = new HistoryResponseDto(
                id, "TestHistory", List.of(new ContractDto("TestContract")));
        when(historyRepository.findById(id)).thenReturn(Optional.of(existing));
        when(contractMapper.toEntityListContract(dto.contractsDtos())).thenReturn(List.of(contract));
        when(historyRepository.save(existing)).thenReturn(existing);
        when(historyMapper.toResponseDto(existing)).thenReturn(responseDto);
        HistoryResponseDto result = service.updateHistory(id, dto);
        assertThat(result.name()).isEqualTo("TestHistory");
        verify(historyMapper).changeHistory(existing,List.of(contract));
    }

    @Test
    void updateHistory_notFound(){
        UUID id = UUID.randomUUID();
        HistoryCreateUpdateDto dto = new HistoryCreateUpdateDto(
                "TestHistory",
                List.of(new ContractDto("TestContract"))
        );
        when(historyRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.updateHistory(id, dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("History not found with id: " + id);
        verify(historyRepository, never()).save(any());
    }


    @Test
    void deleteHistory_success(){
        UUID id = UUID.randomUUID();
        when(historyRepository.existsById(id)).thenReturn(true);
        service.deleteHistory(id);
        verify(historyRepository).deleteById(id);
    }

    @Test
    void deleteHistory_notFound(){
        UUID id = UUID.randomUUID();
        when(historyRepository.existsById(id)).thenReturn(false);
        assertThatThrownBy(() -> service.deleteHistory(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("History not found with id: " + id);
        verify(historyRepository, never()).deleteById(any());
    }

    @Test
    void getHistory_success(){
        UUID id = UUID.randomUUID();
        History history = new History();
        history.setId(id);
        HistoryResponseDto responseDto = new HistoryResponseDto(
                id, "TestHistory", List.of(new ContractDto("TestContract")));
        when(historyRepository.findById(id)).thenReturn(Optional.of(history));
        when(historyMapper.toResponseDto(history)).thenReturn(responseDto);
        HistoryResponseDto result = service.getHistory(id);
        assertThat(result.id()).isEqualTo(id);
    }

    @Test
    void getHistory_notFound(){
        UUID id = UUID.randomUUID();
        when(historyRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getHistory(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("History not found with id: " + id);
    }

}

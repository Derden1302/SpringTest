package ru.springtest.service;

import ru.springtest.domain.Contract;
import ru.springtest.domain.History;
import ru.springtest.dto.ContractCreateUpdateDto;
import ru.springtest.dto.ContractResponseDto;
import ru.springtest.dto.HistoryDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.mapper.ContractMapper;
import ru.springtest.mapper.HistoryMapper;
import ru.springtest.repository.ContractRepository;
import ru.springtest.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import ru.springtest.service.implementation.ContractServiceImplementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractServiceTest {
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private ContractMapper contractMapper;
    @Mock
    private HistoryMapper historyMapper;
    @InjectMocks
    private ContractServiceImplementation service;
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createContract_success() {
        // given
        UUID id = UUID.randomUUID();
        ContractCreateUpdateDto dto = new ContractCreateUpdateDto(
                "TestContract",
                List.of(new HistoryDto("TestHistory"))
        );
        Contract contract = new Contract();
        contract.setId(id);
        History history = new History();
        ContractResponseDto responseDto =
                new ContractResponseDto(id, "TestContract", List.of(new HistoryDto("TestHistory")));
        when(contractMapper.toEntity(dto)).thenReturn(contract);
        when(historyMapper.toEntityListHistory(dto.historyDtos()))
                .thenReturn(List.of(history));
        when(contractRepository.save(contract)).thenReturn(contract);
        when(contractMapper.toResponseDto(contract)).thenReturn(responseDto);
        ContractResponseDto result = service.createContract(dto);
        assertThat(result.id()).isEqualTo(id);
        verify(contractMapper).changeContracts(contract, List.of(history));
    }

    @Test
    void updateContract_success() {
        UUID id = UUID.randomUUID();
        ContractCreateUpdateDto dto = new ContractCreateUpdateDto(
                "Updated Contract",
                List.of(new HistoryDto("TestHistory"))
        );
        Contract existing = new Contract();
        existing.setId(id);
        History history = new History();
        ContractResponseDto responseDto =
                new ContractResponseDto(id, "Updated Contract", List.of(new HistoryDto("TestHistory")));
        when(contractRepository.findById(id)).thenReturn(Optional.of(existing));
        when(historyMapper.toEntityListHistory(dto.historyDtos()))
                .thenReturn(List.of(history));
        when(contractRepository.save(existing)).thenReturn(existing);
        when(contractMapper.toResponseDto(existing)).thenReturn(responseDto);
        ContractResponseDto result = service.updateContract(id, dto);
        assertThat(result.name()).isEqualTo("Updated Contract");
        verify(contractMapper).changeContracts(existing, List.of(history));
    }

    @Test
    void updateContract_notFound() {
        UUID id = UUID.randomUUID();
        ContractCreateUpdateDto dto = new ContractCreateUpdateDto(
                "TestContract", List.of(new HistoryDto("TestHistory"))
        );
        when(contractRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.updateContract(id, dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Contract not found with id: " + id);
        verify(contractRepository, never()).save(any());
    }

    @Test
    void deleteContract_success() {
        UUID id = UUID.randomUUID();
        when(contractRepository.existsById(id)).thenReturn(true);
        service.deleteContract(id);
        verify(contractRepository).deleteById(id);
    }

    @Test
    void deleteContract_notFound() {
        UUID id = UUID.randomUUID();
        when(contractRepository.existsById(id)).thenReturn(false);
        assertThatThrownBy(() -> service.deleteContract(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Contract not found with id: " + id);
        verify(contractRepository, never()).deleteById(any());
    }

    @Test
    void getContract_success() {
        UUID id = UUID.randomUUID();
        Contract entity = new Contract();
        entity.setId(id);
        ContractResponseDto responseDto =
                new ContractResponseDto(id, "TestContract", List.of());
        when(contractRepository.findById(id)).thenReturn(Optional.of(entity));
        when(contractMapper.toResponseDto(entity)).thenReturn(responseDto);
        ContractResponseDto result = service.getContract(id);
        assertThat(result.id()).isEqualTo(id);
    }

    @Test
    void getContract_notFound() {
        UUID id = UUID.randomUUID();
        when(contractRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getContract(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Contract not found with id: " + id);
    }
}

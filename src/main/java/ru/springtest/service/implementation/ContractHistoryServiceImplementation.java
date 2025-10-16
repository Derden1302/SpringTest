package ru.springtest.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.lang.Contract;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.ContractHistory;
import ru.springtest.domain.ContractHistoryId;
import ru.springtest.domain.Contracts;
import ru.springtest.domain.History;
import ru.springtest.dto.*;
import ru.springtest.mapper.ContractHistoryMapper;
import ru.springtest.repository.ContractHistoryRepository;
import ru.springtest.repository.ContractsRepository;
import ru.springtest.repository.HistoryRepository;
import ru.springtest.service.ContractHistoryService;
import ru.springtest.service.UserService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ContractHistoryServiceImplementation implements ContractHistoryService {
    private final ContractHistoryRepository contractHistoryRepository;
    private final ContractsRepository contractsRepository;
    private final HistoryRepository historyRepository;
    private final ContractHistoryMapper mapper;

    @Override
    public Contracts createContract(ContractCreateDto dto) {
        Contracts contract = new Contracts();
        contract.setName(dto.name());
        return contractsRepository.save(contract);
    }

    @Override
    public History createHistory(HistoryCreateDto dto){
        History history = new History();
        history.setName(dto.name());
        return historyRepository.save(history);
    }

    @Override
    public ContractHistoryResponseDto createOrUpdate(ContractHistoryCreateUpdateDto dto) {
        Contracts contract = contractsRepository.findById(dto.contractId())
                .orElseThrow(() -> new EntityNotFoundException("Contract not found: " + dto.contractId()));
        History history = historyRepository.findById(dto.historyId())
                .orElseThrow(() -> new EntityNotFoundException("History not found: " + dto.historyId()));
        ContractHistoryId id = new ContractHistoryId(dto.contractId(), dto.historyId());
        ContractHistory entity = contractHistoryRepository.findById(id)
                .orElseGet(() -> {
                    ContractHistory contractHistory = new ContractHistory();
                    contractHistory.setContract(contract);
                    contractHistory.setHistory(history);
                    contractHistory.setId(id);
                    return contractHistory;
                });
        entity.setEventDate(dto.eventDate());
        ContractHistory saved = contractHistoryRepository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public ContractHistoryResponseDto getByKey (ContractHistoryKeyDto key) {
        ContractHistoryId id = new ContractHistoryId(key.contractId(), key.historyId());
        ContractHistory contractHistory = contractHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pair not found: " + id));
        return mapper.toDto(contractHistory);
    }

    @Override
    public void deleteByKey (ContractHistoryKeyDto key) {
        ContractHistoryId id = new ContractHistoryId(key.contractId(), key.historyId());
        if (!contractHistoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Pair not found: " + id.toString());
        }
        else contractHistoryRepository.deleteById(id);
    }

    @Override
    public List<ContractHistoryResponseDto> listByContract(UUID contractId) {
        return contractHistoryRepository.findAllByContract_Id(contractId)
                .stream().map(mapper::toDto).toList();
    }

    @Override
    public List<ContractHistoryResponseDto> listByHistory(UUID historyId) {
        return contractHistoryRepository.findAllByHistory_Id(historyId)
                .stream().map(mapper::toDto).toList();
    }



}

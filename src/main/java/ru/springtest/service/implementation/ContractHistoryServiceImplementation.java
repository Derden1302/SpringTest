package ru.springtest.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractHistoryServiceImplementation implements ContractHistoryService {
    private final ContractHistoryRepository contractHistoryRepository;
    private final ContractsRepository contractsRepository;
    private final HistoryRepository historyRepository;
    private final ContractHistoryMapper mapper;

    @Transactional
    @Override
    public Contracts createContract(ContractCreateUpdateDto dto) {
        Contracts contract = mapper.contractsToEntity(dto);
        return contractsRepository.save(contract);
    }

    @Transactional
    @Override
    public History createHistory(HistoryCreateUpdateDto dto){
        History history = mapper.historyToEntity(dto);
        return historyRepository.save(history);
    }

    @Transactional
    @Override
    public Contracts updateContract(UUID id, ContractCreateUpdateDto dto) {
        Contracts contracts = contractsRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Contract not found"));
        mapper.contractsUpdateEntity(contracts, dto);
        return contractsRepository.save(contracts);
    }

    @Transactional
    @Override
    public History updateHistory(UUID id, HistoryCreateUpdateDto dto) {
        History history = historyRepository.findById(id).orElseThrow(()->new EntityNotFoundException("History not found"));
        mapper.historyUpdateEntity(history, dto);
        return historyRepository.save(history);
    }

    @Transactional
    @Override
    public ContractHistoryResponseDto create(ContractHistoryCreateUpdateDto dto) {
        Contracts contract = contractsRepository.findById(dto.contractId())
                .orElseThrow(() -> new EntityNotFoundException("Contract not found: " + dto.contractId()));
        History history = historyRepository.findById(dto.historyId())
                .orElseThrow(() -> new EntityNotFoundException("History not found: " + dto.historyId()));
        ContractHistoryId id = new ContractHistoryId(dto.contractId(), dto.historyId());
        ContractHistory saved = mapper.contractHistoryToEntity(id, contract, history);
        contractHistoryRepository.save(saved);
        return mapper.toDto(saved);
    }

    @Transactional
    @Override
    public ContractHistoryResponseDto update(UUID contractId, UUID historyId, ContractHistoryCreateUpdateDto dto){
        ContractHistoryId id = new ContractHistoryId(contractId, historyId);
        ContractHistory contractHistory = contractHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pair not found: " + id));
        mapper.contractHistoryUpdateEntity(contractHistory, dto);
        return mapper.toDto(contractHistoryRepository.save(contractHistory));
    }

    @Transactional
    @Override
    public ContractHistoryResponseDto getByKey (ContractHistoryKeyDto key) {
        ContractHistoryId id = new ContractHistoryId(key.contractId(), key.historyId());
        ContractHistory contractHistory = contractHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pair not found: " + id));
        return mapper.toDto(contractHistory);
    }

    @Transactional
    @Override
    public void deleteByKey (ContractHistoryKeyDto key) {
        ContractHistoryId id = new ContractHistoryId(key.contractId(), key.historyId());
        if (!contractHistoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Pair not found: " + id.toString());
        }
        else contractHistoryRepository.deleteById(id);
    }

    @Transactional
    @Override
    public List<ContractHistoryResponseDto> listByContract(UUID contractId) {
        return contractHistoryRepository.findAllByContract_Id(contractId)
                .stream().map(mapper::toDto).toList();
    }

    @Transactional
    @Override
    public List<ContractHistoryResponseDto> listByHistory(UUID historyId) {
        return contractHistoryRepository.findAllByHistory_Id(historyId)
                .stream().map(mapper::toDto).toList();
    }

}

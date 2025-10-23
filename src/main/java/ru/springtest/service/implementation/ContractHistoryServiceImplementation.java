package ru.springtest.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.Contract;
import ru.springtest.domain.History;
import ru.springtest.dto.*;
import ru.springtest.mapper.ContractHistoryMapper;
import ru.springtest.repository.ContractRepository;
import ru.springtest.repository.HistoryRepository;
import ru.springtest.service.ContractHistoryService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractHistoryServiceImplementation implements ContractHistoryService {
    private final ContractRepository contractRepository;
    private final HistoryRepository historyRepository;
    private final ContractHistoryMapper mapper;

    @Transactional
    @Override
    public Contract createContract(ContractCreateUpdateDto dto) {
        Contract contract = mapper.toEntity(dto);
        return contractRepository.save(contract);
    }

    @Transactional
    @Override
    public History createHistory(HistoryCreateUpdateDto dto){
        History history = mapper.toEntity(dto);
        return historyRepository.save(history);
    }

    @Transactional
    @Override
    public Contract updateContract(UUID id, ContractCreateUpdateDto dto) {
        Contract contract = contractRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Contract not found"));
        mapper.changeContracts(contract, dto);
        return contractRepository.save(contract);
    }

    @Transactional
    @Override
    public History updateHistory(UUID id, HistoryCreateUpdateDto dto) {
        History history = historyRepository.findById(id).orElseThrow(()->new EntityNotFoundException("History not found"));
        mapper.changeHistory(history, dto);
        return historyRepository.save(history);
    }

    @Transactional
    @Override
    public void deleteContract (UUID id) {
        if (!contractRepository.existsById(id)) {
            throw new EntityNotFoundException("Pair not found: " + id.toString());
        }
        else contractRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteHistory (UUID id) {
        if (!historyRepository.existsById(id)) {
            throw new EntityNotFoundException("Pair not found: " + id.toString());
        }
        else historyRepository.deleteById(id);
    }

    @Transactional
    @Override
    public ContractResponseDto getContract(UUID contractId) {
        List<HistoryDto> histories = historyRepository.findAllByContract_Id(contractId)
                .stream().map(mapper::toDto).toList();
        contractRepository.findById(contractId);
        Contract contract = contractRepository.findById(contractId).orElseThrow(()->new EntityNotFoundException("Contract not found"));
        return mapper.toDto(contract, histories);
    }

    @Transactional
    @Override
    public HistoryResponseDto getHistory(UUID historyId) {
        List<ContractDto> contracts = contractRepository.findAllByHistory_Id(historyId)
                .stream().map(mapper::toDto).toList();
        History history = historyRepository.findById(historyId).orElseThrow(()->new EntityNotFoundException("History not found"));
        return mapper.toDto(history, contracts);
    }

}

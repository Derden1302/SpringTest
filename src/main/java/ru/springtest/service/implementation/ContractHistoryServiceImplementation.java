package ru.springtest.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.Contract;
import ru.springtest.domain.History;
import ru.springtest.dto.*;
import ru.springtest.exception.NotFoundException;
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
    public ContractResponseDto createContract(ContractCreateUpdateDto dto) {
        Contract contract = mapper.toEntity(dto);
        List<UUID> ids = dto.historyIds();
        if (ids != null && !ids.isEmpty()) {
            List<History> histories = historyRepository.findAllById(ids);
            mapper.changeContracts(contract, dto, histories);};
        contractRepository.save(contract);
        List<HistoryDto> histories = historyRepository.findAllByContract_Id(contract.getId())
                .stream().map(mapper::toDto).toList();
        return mapper.toDto(contract,histories);
    }

    @Transactional
    @Override
    public HistoryResponseDto createHistory(HistoryCreateUpdateDto dto){
        History history = mapper.toEntity(dto);
        List<UUID> ids = dto.contractsIds();
        if (ids != null && !ids.isEmpty()) {
            List<Contract> contracts = contractRepository.findAllById(ids);
            mapper.changeHistory(history, dto, contracts);};
        historyRepository.save(history);
        List<ContractDto> contracts = contractRepository.findAllByHistory_Id(history.getId())
                .stream().map(mapper::toDto).toList();
        return mapper.toDto(history, contracts);
    }

    @Transactional
    @Override
    public ContractResponseDto updateContract(UUID id, ContractCreateUpdateDto dto) {
        Contract contract = contractRepository.findById(id).orElseThrow(()->new NotFoundException("Contract not found with id:" + id));
        List<History> history = historyRepository.findAllById(dto.historyIds());
        mapper.changeContracts(contract, dto, history);
        contractRepository.save(contract);
        List<HistoryDto> historyDto = history.stream().map(mapper::toDto).toList();
        return mapper.toDto(contract, historyDto);
    }

    @Transactional
    @Override
    public HistoryResponseDto updateHistory(UUID id, HistoryCreateUpdateDto dto) {
        History history = historyRepository.findById(id).orElseThrow(()->new NotFoundException("History not found with id:" + id));
        List<Contract> contract = contractRepository.findAllById(dto.contractsIds());
        mapper.changeHistory(history, dto, contract);
        historyRepository.save(history);
        List<ContractDto> contractDto=contract.stream().map(mapper::toDto).toList();
        return mapper.toDto(history, contractDto);
    }

    @Transactional
    @Override
    public void deleteContract (UUID id) {
        if (!contractRepository.existsById(id)) {
            throw new NotFoundException("Contract not found with id:" + id);
        }
        else contractRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteHistory (UUID id) {
        if (!historyRepository.existsById(id)) {
            throw new NotFoundException("History not found with id: " + id.toString());
        }
        else historyRepository.deleteById(id);
    }

    @Transactional
    @Override
    public ContractResponseDto getContract(UUID contractId) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(()->new NotFoundException("Contract not found with id:" + contractId));
        List<HistoryDto> histories = historyRepository.findAllByContract_Id(contractId)
                .stream().map(mapper::toDto).toList();
        return mapper.toDto(contract, histories);
    }

    @Transactional
    @Override
    public HistoryResponseDto getHistory(UUID historyId) {
        History history = historyRepository.findById(historyId).orElseThrow(()->new NotFoundException("History not found with id: " + historyId));
        List<ContractDto> contracts = contractRepository.findAllByHistory_Id(historyId)
                .stream().map(mapper::toDto).toList();
        return mapper.toDto(history, contracts);
    }

}

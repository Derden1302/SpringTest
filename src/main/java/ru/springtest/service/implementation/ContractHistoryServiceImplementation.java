package ru.springtest.service.implementation;

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

import java.util.ArrayList;
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
        List<History> histories = getListHistories(dto.historyDtos());
        mapper.changeContracts(contract, histories);
        return mapper.toResponseDto(contractRepository.save(contract));
    }

    @Transactional
    @Override
    public HistoryResponseDto createHistory(HistoryCreateUpdateDto dto){
        History history = mapper.toEntity(dto);
        List<Contract> contracts = getListContracts(dto.contractsDtos());
        mapper.changeHistory(history, contracts);
        return mapper.toResponseDto(historyRepository.save(history));
    }

    @Transactional
    @Override
    public List<History> getListHistories(List<HistoryDto> historyDtos) {
        List<History> histories = new ArrayList<>();
        for (HistoryDto historyDto : historyDtos) {
            if (historyDto.id() != null) {
                History history = historyRepository.findById(historyDto.id())
                        .orElseThrow(()-> new NotFoundException("History not found with id:" + historyDto.id()));
                histories.add(history);
            } else {
                History history = mapper.toEntity(historyDto);
                histories.add(historyRepository.save(history));
            }
        }
        return histories;
    }

    @Transactional
    @Override
    public List<Contract> getListContracts(List<ContractDto> contractDtos) {
        List<Contract> contracts = new ArrayList<>();
        for (ContractDto contractDto : contractDtos) {
            if (contractDto.id() != null) {
                Contract contract = contractRepository.findById(contractDto.id())
                        .orElseThrow(()-> new NotFoundException("Contract not found with id:" + contractDto.id()));
                contracts.add(contract);
            }  else {
                Contract contract = mapper.toEntity(contractDto);
                contracts.add(contract);
            }
        }
        return contracts;
    }

    @Transactional
    @Override
    public ContractResponseDto updateContract(UUID id, ContractCreateUpdateDto dto) {
        Contract contract = contractRepository.findById(id).orElseThrow(()->new NotFoundException("Contract not found with id:" + id));
        List<History> histories = getListHistories(dto.historyDtos());
        mapper.changeContracts(contract, dto, histories);
        return mapper.toResponseDto(contractRepository.save(contract));
    }

    @Transactional
    @Override
    public HistoryResponseDto updateHistory(UUID id, HistoryCreateUpdateDto dto) {
        History history = historyRepository.findById(id).orElseThrow(()->new NotFoundException("History not found with id:" + id));
        List<Contract> contracts = getListContracts(dto.contractsDtos());
        mapper.changeHistory(history, dto, contracts);
        return mapper.toResponseDto(historyRepository.save(history));
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
        return mapper.toResponseDto(contract);
    }

    @Transactional
    @Override
    public HistoryResponseDto getHistory(UUID historyId) {
        History history = historyRepository.findById(historyId).orElseThrow(()->new NotFoundException("History not found with id: " + historyId));
        return mapper.toResponseDto(history);
    }

}

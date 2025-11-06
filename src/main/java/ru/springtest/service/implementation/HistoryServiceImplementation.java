package ru.springtest.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.Contract;
import ru.springtest.domain.History;
import ru.springtest.dto.HistoryCreateUpdateDto;
import ru.springtest.dto.HistoryDto;
import ru.springtest.dto.HistoryResponseDto;
import ru.springtest.exception.NotFoundException;
import ru.springtest.mapper.ContractMapper;
import ru.springtest.mapper.HistoryMapper;
import ru.springtest.repository.ContractRepository;
import ru.springtest.repository.HistoryRepository;
import ru.springtest.service.ContractService;
import ru.springtest.service.HistoryService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HistoryServiceImplementation implements HistoryService {
    private final HistoryRepository historyRepository;
    private final ContractMapper contractMapper;
    private final HistoryMapper historyMapper;


    @Transactional
    @Override
    public HistoryResponseDto createHistory(HistoryCreateUpdateDto dto){
        History history = historyMapper.toEntity(dto);
        List<Contract> contracts = contractMapper.toEntityListContract(dto.contractsDtos());
        historyMapper.changeHistory(history, contracts);
        return historyMapper.toResponseDto(historyRepository.save(history));
    }

    @Transactional
    @Override
    public HistoryResponseDto updateHistory(UUID id, HistoryCreateUpdateDto dto) {
        History history = historyRepository.findById(id).orElseThrow(()->new NotFoundException("History not found with id:" + id));
        List<Contract> contracts = contractMapper.toEntityListContract(dto.contractsDtos());
        historyMapper.changeHistory(history, contracts);
        return historyMapper.toResponseDto(historyRepository.save(history));
    }

    @Transactional
    @Override
    public void deleteHistory(UUID id) {
        if (!historyRepository.existsById(id)) {
            throw new NotFoundException("History not found with id: " + id.toString());
        }
        historyRepository.deleteById(id);
    }

    @Transactional
    @Override
    public HistoryResponseDto getHistory(UUID historyId) {
        History history = historyRepository.findById(historyId).orElseThrow(()->new NotFoundException("History not found with id: " + historyId));
        return historyMapper.toResponseDto(history);
    }

}

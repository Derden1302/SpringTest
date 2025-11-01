package ru.springtest.service;

import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.Contract;
import ru.springtest.domain.History;
import ru.springtest.dto.*;

import java.util.List;
import java.util.UUID;

public interface ContractHistoryService {
    ContractResponseDto createContract(ContractCreateUpdateDto contract);
    HistoryResponseDto createHistory(HistoryCreateUpdateDto history);
    HistoryResponseDto updateHistory(UUID id, HistoryCreateUpdateDto dto);
    List<History> getListHistories(List<HistoryDto> historyDtos);
    List<Contract> getListContracts(List<ContractDto> contractDtos);
    ContractResponseDto updateContract(UUID id, ContractCreateUpdateDto dto);
    void deleteHistory (UUID id);
    void deleteContract (UUID id);
    ContractResponseDto getContract(UUID contractId);
    HistoryResponseDto getHistory(UUID historyId);

}

package ru.springtest.service;

import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.Contracts;
import ru.springtest.domain.History;
import ru.springtest.dto.*;

import java.util.List;
import java.util.UUID;

public interface ContractHistoryService {
    Contracts createContract(ContractCreateUpdateDto contract);
    History createHistory(HistoryCreateUpdateDto history);
    History updateHistory(UUID id, HistoryCreateUpdateDto dto);
    Contracts updateContract(UUID id, ContractCreateUpdateDto dto);
    ContractHistoryResponseDto create(ContractHistoryCreateUpdateDto dto);
    ContractHistoryResponseDto update(UUID contractId, UUID historyId, ContractHistoryCreateUpdateDto dto);
    ContractHistoryResponseDto getByKey(ContractHistoryKeyDto key);
    void deleteByKey(ContractHistoryKeyDto key);
    List<ContractHistoryResponseDto> listByContract(UUID contractId);
    List<ContractHistoryResponseDto> listByHistory(UUID historyId);

}

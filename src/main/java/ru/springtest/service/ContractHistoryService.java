package ru.springtest.service;

import ru.springtest.domain.Contract;
import ru.springtest.domain.History;
import ru.springtest.dto.*;

import java.util.List;
import java.util.UUID;

public interface ContractHistoryService {
    Contract createContract(ContractCreateUpdateDto contract);
    History createHistory(HistoryCreateUpdateDto history);
    History updateHistory(UUID id, HistoryCreateUpdateDto dto);
    Contract updateContract(UUID id, ContractCreateUpdateDto dto);
    void deleteHistory (UUID id);
    void deleteContract (UUID id);
    ContractResponseDto getContract(UUID contractId);
    HistoryResponseDto getHistory(UUID historyId);

}

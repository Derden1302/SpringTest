package ru.springtest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Contract;
import org.springframework.stereotype.Service;
import ru.springtest.domain.Contracts;
import ru.springtest.domain.History;
import ru.springtest.dto.*;

import java.util.List;
import java.util.UUID;

public interface ContractHistoryService {
    Contracts createContract(ContractCreateDto contract);
    History createHistory(HistoryCreateDto history);
    ContractHistoryResponseDto createOrUpdate(ContractHistoryCreateUpdateDto dto);
    ContractHistoryResponseDto getByKey(ContractHistoryKeyDto key);
    void deleteByKey(ContractHistoryKeyDto key);

    List<ContractHistoryResponseDto> listByContract(UUID contractId);
    List<ContractHistoryResponseDto> listByHistory(UUID historyId);

}

package ru.springtest.service;

import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.Contract;
import ru.springtest.domain.History;
import ru.springtest.dto.*;

import java.util.List;
import java.util.UUID;

public interface ContractService {
    ContractResponseDto createContract(ContractCreateUpdateDto contract);
    ContractResponseDto updateContract(UUID id, ContractCreateUpdateDto dto);
    void deleteContract (UUID id);
    ContractResponseDto getContract(UUID contractId);

}

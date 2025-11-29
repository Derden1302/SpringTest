package ru.springtest.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springtest.domain.Contract;
import ru.springtest.domain.History;
import ru.springtest.dto.*;
import ru.springtest.exception.NotFoundException;
import ru.springtest.mapper.ContractMapper;
import ru.springtest.mapper.HistoryMapper;
import ru.springtest.repository.ContractRepository;
import ru.springtest.service.ContractService;
import ru.springtest.service.HistoryService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractServiceImplementation implements ContractService {
    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final HistoryMapper historyMapper;

    @CachePut(value = "contract", key = "#result.id()")
    @Transactional
    @Override
    public ContractResponseDto createContract(ContractCreateUpdateDto dto) {
        Contract contract = contractMapper.toEntity(dto);
        List<History> histories= historyMapper.toEntityListHistory(dto.historyDtos());
        contractMapper.changeContracts(contract, dto, histories);
        return contractMapper.toResponseDto(contractRepository.save(contract));
    }

    @CachePut(value = "contract", key = "#result.id()")
    @Transactional
    @Override
    public ContractResponseDto updateContract(UUID id, ContractCreateUpdateDto dto) {
        Contract contract = contractRepository.findById(id).orElseThrow(()->new NotFoundException("Contract not found with id: " + id));
        List<History> histories= historyMapper.toEntityListHistory(dto.historyDtos());
        contractMapper.changeContracts(contract, dto, histories);
        return contractMapper.toResponseDto(contractRepository.save(contract));
    }

    @CacheEvict(value = "contract", key = "#id")
    @Transactional
    @Override
    public void deleteContract (UUID id) {
        if (!contractRepository.existsById(id)) {
            throw new NotFoundException("Contract not found with id: " + id);
        }
        contractRepository.deleteById(id);
    }

    @Cacheable(value = "contract", key = "#id")
    @Transactional
    @Override
    public ContractResponseDto getContract(UUID id) {
        Contract contract = contractRepository.findById(id).orElseThrow(()->new NotFoundException("Contract not found with id: " + id));
        return contractMapper.toResponseDto(contract);
    }


}

package ru.springtest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Contract;
import org.springframework.web.bind.annotation.*;
import ru.springtest.domain.ContractHistory;
import ru.springtest.domain.Contracts;
import ru.springtest.domain.History;
import ru.springtest.dto.*;
import ru.springtest.repository.ContractHistoryRepository;
import ru.springtest.service.ContractHistoryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value="contract-history/v1/api")
@Slf4j
@RequiredArgsConstructor
public class ContractHistoryController {
    private final ContractHistoryService contractHistoryService;

    @PostMapping("/contract")
    public ResponseEntity<Contracts> createContract(@RequestBody ContractCreateDto contract) {
        return ResponseEntity.ok(contractHistoryService.createContract(contract));
    }

    @PostMapping("/history")
    public ResponseEntity<History> createHistory(@RequestBody HistoryCreateDto history) {
        return ResponseEntity.ok(contractHistoryService.createHistory(history));
    }

    @PostMapping("/contract-history")
    public ResponseEntity<ContractHistoryResponseDto> createContractHistory(@RequestBody ContractHistoryCreateUpdateDto dto) {
        return ResponseEntity.ok(contractHistoryService.createOrUpdate(dto));
    }

    @GetMapping("/by-pair-key")
    public ResponseEntity<ContractHistoryResponseDto> getContractHistoryByPairKey
            (@RequestParam UUID contractId, @RequestParam UUID historyId) {
        return ResponseEntity.ok(contractHistoryService.getByKey(new ContractHistoryKeyDto(contractId, historyId)));
    }

    @DeleteMapping
    public void delete(@RequestParam UUID contractId, @RequestParam UUID historyId) {
        contractHistoryService.deleteByKey(new ContractHistoryKeyDto(contractId, historyId));
    }

    @GetMapping("/by-contract")
    public List<ContractHistoryResponseDto> listByContract(@RequestParam UUID contractId) {
        return contractHistoryService.listByContract(contractId);
    }

    @GetMapping("/by-history")
    public List<ContractHistoryResponseDto> listByHistory(@RequestParam UUID historyId) {
        return contractHistoryService.listByHistory(historyId);
    }


}

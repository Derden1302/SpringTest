package ru.springtest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.springtest.domain.Contracts;
import ru.springtest.domain.History;
import ru.springtest.dto.*;
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
    public ResponseEntity<Contracts> createContract(@RequestBody ContractCreateUpdateDto contract) {
        return ResponseEntity.ok(contractHistoryService.createContract(contract));
    }

    @PostMapping("/history")
    public ResponseEntity<History> createHistory(@RequestBody HistoryCreateUpdateDto history) {
        return ResponseEntity.ok(contractHistoryService.createHistory(history));
    }

    @PostMapping("/contract-history")
    public ResponseEntity<ContractHistoryResponseDto> createContractHistory(@RequestBody ContractHistoryCreateUpdateDto dto) {
        return ResponseEntity.ok(contractHistoryService.create(dto));
    }

    @PutMapping("/update/contract/{id}")
    public ResponseEntity<Contracts> updateContract(@PathVariable UUID id, @RequestBody ContractCreateUpdateDto contract) {
        return ResponseEntity.ok(contractHistoryService.updateContract(id,contract));
    }

    @PutMapping("/update/history/{id}")
    public ResponseEntity<History> updateHistory(@PathVariable UUID id, @RequestBody HistoryCreateUpdateDto history) {
        return ResponseEntity.ok(contractHistoryService.updateHistory(id,history));
    }

    @PutMapping("/update-pair/{contractId}/{historyId}")
    public ResponseEntity<ContractHistoryResponseDto> updateContractHistory(@PathVariable UUID contractId,
                                                                            @PathVariable UUID historyId,
                                                                            @RequestBody ContractHistoryCreateUpdateDto dto) {
        return ResponseEntity.ok(contractHistoryService.update(contractId,historyId,dto));
    }


    @GetMapping("/by-pair-key/{contractId}/{historyId}")
    public ResponseEntity<ContractHistoryResponseDto> getContractHistoryByPairKey
            (@PathVariable UUID contractId, @PathVariable UUID historyId) {
        return ResponseEntity.ok(contractHistoryService.getByKey(new ContractHistoryKeyDto(contractId, historyId)));
    }

    @DeleteMapping("/{contractId}/{historyId}")
    public void delete(@PathVariable UUID contractId, @PathVariable UUID historyId) {
        contractHistoryService.deleteByKey(new ContractHistoryKeyDto(contractId, historyId));
    }

    @GetMapping("/by-contract/{contractId}")
    public List<ContractHistoryResponseDto> listByContract(@PathVariable UUID contractId) {
        return contractHistoryService.listByContract(contractId);
    }

    @GetMapping("/by-history/{historyId}")
    public List<ContractHistoryResponseDto> listByHistory(@PathVariable UUID historyId) {
        return contractHistoryService.listByHistory(historyId);
    }


}

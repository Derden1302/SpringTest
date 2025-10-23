package ru.springtest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.springtest.domain.Contract;
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
    public ResponseEntity<Contract> createContract(@RequestBody ContractCreateUpdateDto contract) {
        return ResponseEntity.ok(contractHistoryService.createContract(contract));
    }

    @PostMapping("/history")
    public ResponseEntity<History> createHistory(@RequestBody HistoryCreateUpdateDto history) {
        return ResponseEntity.ok(contractHistoryService.createHistory(history));
    }


    @PutMapping("/update/contract/{id}")
    public ResponseEntity<Contract> updateContract(@PathVariable UUID id, @RequestBody ContractCreateUpdateDto contract) {
        return ResponseEntity.ok(contractHistoryService.updateContract(id,contract));
    }

    @PutMapping("/update/history/{id}")
    public ResponseEntity<History> updateHistory(@PathVariable UUID id, @RequestBody HistoryCreateUpdateDto history) {
        return ResponseEntity.ok(contractHistoryService.updateHistory(id,history));
    }

    @DeleteMapping("/contract/{contractId}")
    public void deleteContract(@PathVariable UUID contractId) {
        contractHistoryService.deleteContract(contractId);
    }

    @DeleteMapping("/history/{historyId}")
    public void deleteHistory(@PathVariable UUID historyId) {
        contractHistoryService.deleteHistory(historyId);
    }

    @GetMapping("/contract/{contractId}")
    public ContractResponseDto getContract (@PathVariable UUID contractId) {
        return contractHistoryService.getContract(contractId);
    }

    @GetMapping("/history/{historyId}")
    public HistoryResponseDto getHistory(@PathVariable UUID historyId) {
        return contractHistoryService.getHistory(historyId);
    }

}

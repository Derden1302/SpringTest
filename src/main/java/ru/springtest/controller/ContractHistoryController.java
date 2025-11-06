package ru.springtest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.springtest.dto.*;
import ru.springtest.service.ContractService;
import ru.springtest.service.HistoryService;

import java.util.UUID;

@RestController
@RequestMapping(value="contract-history/v2/api")
@Slf4j
@RequiredArgsConstructor
public class ContractHistoryController {
    private final ContractService contractService;
    private final HistoryService historyService;

    @PostMapping("/contract")
    public ResponseEntity<ContractResponseDto> createContract(@RequestBody ContractCreateUpdateDto contract) {
        return ResponseEntity.status(HttpStatus.CREATED).body((contractService.createContract(contract)));
    }

    @PostMapping("/history")
    public ResponseEntity<HistoryResponseDto> createHistory(@RequestBody HistoryCreateUpdateDto history) {
        return ResponseEntity.status(HttpStatus.CREATED).body((historyService.createHistory(history)));
    }

    @PutMapping("/contract/{id}")
    public ResponseEntity<ContractResponseDto> updateContract(@PathVariable UUID id, @RequestBody ContractCreateUpdateDto contract) {
        return ResponseEntity.ok(contractService.updateContract(id,contract));
    }

    @PutMapping("/history/{id}")
    public ResponseEntity<HistoryResponseDto> updateHistory(@PathVariable UUID id, @RequestBody HistoryCreateUpdateDto history) {
        return ResponseEntity.ok(historyService.updateHistory(id,history));
    }

    @DeleteMapping("/contract/{contractId}")
    public ResponseEntity<Void> deleteContract(@PathVariable UUID contractId) {
        contractService.deleteContract(contractId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/history/{historyId}")
    public ResponseEntity<Void> deleteHistory(@PathVariable UUID historyId) {
        historyService.deleteHistory(historyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/contract/{contractId}")
    public ResponseEntity<ContractResponseDto> getContract (@PathVariable UUID contractId) {
        return ResponseEntity.ok(contractService.getContract(contractId)) ;
    }

    @GetMapping("/history/{historyId}")
    public ResponseEntity<HistoryResponseDto> getHistory(@PathVariable UUID historyId) {
        return ResponseEntity.ok(historyService.getHistory(historyId));
    }

}

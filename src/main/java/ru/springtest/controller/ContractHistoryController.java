package ru.springtest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ContractResponseDto> createContract(@RequestBody ContractCreateUpdateDto contract) {
        return ResponseEntity.status(HttpStatus.CREATED).body((contractHistoryService.createContract(contract)));
    }

    @PostMapping("/history")
    public ResponseEntity<HistoryResponseDto> createHistory(@RequestBody HistoryCreateUpdateDto history) {
        return ResponseEntity.status(HttpStatus.CREATED).body((contractHistoryService.createHistory(history)));
    }


    @PutMapping("/update/contract/{id}")
    public ResponseEntity<ContractResponseDto> updateContract(@PathVariable UUID id, @RequestBody ContractCreateUpdateDto contract) {
        return ResponseEntity.ok(contractHistoryService.updateContract(id,contract));
    }

    @PutMapping("/update/history/{id}")
    public ResponseEntity<HistoryResponseDto> updateHistory(@PathVariable UUID id, @RequestBody HistoryCreateUpdateDto history) {
        return ResponseEntity.ok(contractHistoryService.updateHistory(id,history));
    }

    @DeleteMapping("/contract/{contractId}")
    public ResponseEntity<Void> deleteContract(@PathVariable UUID contractId) {
        contractHistoryService.deleteContract(contractId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/history/{historyId}")
    public ResponseEntity<Void> deleteHistory(@PathVariable UUID historyId) {
        contractHistoryService.deleteHistory(historyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/contract/{contractId}")
    public ResponseEntity<ContractResponseDto> getContract (@PathVariable UUID contractId) {
        return ResponseEntity.ok(contractHistoryService.getContract(contractId)) ;
    }

    @GetMapping("/history/{historyId}")
    public ResponseEntity<HistoryResponseDto> getHistory(@PathVariable UUID historyId) {
        return ResponseEntity.ok(contractHistoryService.getHistory(historyId));
    }

}

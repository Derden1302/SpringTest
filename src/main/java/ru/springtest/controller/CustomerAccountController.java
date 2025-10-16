package ru.springtest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.springtest.dto.CustomerAccountCreateUpdateDto;
import ru.springtest.dto.CustomerAccountResponseDto;
import ru.springtest.service.CustomersAccountsService;

import java.util.UUID;

@RestController
@RequestMapping(value="customer-account/v1/api")
@Slf4j
@RequiredArgsConstructor
public class CustomerAccountController {
    private final CustomersAccountsService customersAccountsService;

    @PostMapping("/create-pair")
    public ResponseEntity<CustomerAccountResponseDto> createWithAccount(@RequestBody CustomerAccountCreateUpdateDto dto) {
        return ResponseEntity.ok(customersAccountsService.createWithAccount(dto));
    }

    @PutMapping("/update-pair")
    public ResponseEntity<CustomerAccountResponseDto> updateWithAccount(@RequestBody CustomerAccountCreateUpdateDto dto) {
        return ResponseEntity.ok(customersAccountsService.updateWithAccount(dto));
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<CustomerAccountResponseDto> getWithId(UUID id) {
        return ResponseEntity.ok(customersAccountsService.getById(id));
    }

    @DeleteMapping("/delete-by-id")
    public void deleteById(UUID id) {
        customersAccountsService.delete(id);
    }

}

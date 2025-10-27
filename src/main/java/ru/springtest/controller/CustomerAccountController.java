package ru.springtest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/")
    public ResponseEntity<CustomerAccountResponseDto> createWithAccount(@RequestBody CustomerAccountCreateUpdateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body((customersAccountsService.createWithAccount(dto)));
    }

    @PutMapping("/")
    public ResponseEntity<CustomerAccountResponseDto> updateWithAccount(@PathVariable UUID id, @RequestBody CustomerAccountCreateUpdateDto dto) {
        return ResponseEntity.ok(customersAccountsService.updateWithAccount(dto, id));
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<CustomerAccountResponseDto> getWithId(@PathVariable UUID id) {
        return ResponseEntity.ok(customersAccountsService.getById(id));
    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        customersAccountsService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

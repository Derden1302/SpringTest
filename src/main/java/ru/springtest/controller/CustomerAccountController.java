package ru.springtest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.springtest.dto.CustomerAccountCreateUpdateDto;
import ru.springtest.dto.CustomerAccountResponseDto;
import ru.springtest.service.CustomerAccountService;

import java.util.UUID;

@RestController
@RequestMapping(value="customer-account/v2/api")
@Slf4j
@RequiredArgsConstructor
public class CustomerAccountController {
    private final CustomerAccountService customerAccountService;

    @PostMapping("/сustomer")
    public ResponseEntity<CustomerAccountResponseDto> createWithAccount(@RequestBody @Valid CustomerAccountCreateUpdateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body((customerAccountService.createWithAccount(dto)));
    }

    @PutMapping("/customer/{id}")
    public ResponseEntity<CustomerAccountResponseDto> updateWithAccount(@PathVariable UUID id, @RequestBody @Valid CustomerAccountCreateUpdateDto dto) {
        return ResponseEntity.ok(customerAccountService.updateWithAccount(dto, id));
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<CustomerAccountResponseDto> getWithId(@PathVariable UUID id) {
        return ResponseEntity.ok(customerAccountService.getById(id));
    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        customerAccountService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

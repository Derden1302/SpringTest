package ru.springtest.service;

import ru.springtest.dto.CustomerAccountCreateUpdateDto;
import ru.springtest.dto.CustomerAccountResponseDto;

import java.util.UUID;

public interface CustomerAccountService {
    CustomerAccountResponseDto createWithAccount(CustomerAccountCreateUpdateDto dto);
    CustomerAccountResponseDto getById(UUID id);
    CustomerAccountResponseDto updateWithAccount(CustomerAccountCreateUpdateDto dto,  UUID id);
    void delete(UUID id);
}
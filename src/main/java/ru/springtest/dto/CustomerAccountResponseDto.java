package ru.springtest.dto;

import java.util.UUID;

public record CustomerAccountResponseDto(
        UUID customerId,
        String customerName,
        String accountData
) {
}

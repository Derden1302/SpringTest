package ru.springtest.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ContractHistoryResponseDto(
        UUID contractId,
        UUID historyId,
        String contractName,
        String historyName,
        LocalDateTime eventDate
) {
}

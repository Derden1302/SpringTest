package ru.springtest.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ContractHistoryCreateUpdateDto(
        UUID contractId,
        UUID historyId,
        LocalDateTime eventDate
) {
}

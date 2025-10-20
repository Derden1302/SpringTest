package ru.springtest.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.UUID;

public record ContractHistoryCreateUpdateDto(
        @NotBlank(message = "Айди отсутствуе")
        UUID contractId,
        @NotBlank(message = "Айди отсутствуе")
        UUID historyId,
        @NotBlank(message = "Дата отсутствует")
        LocalDateTime eventDate
) {
}

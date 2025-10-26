package ru.springtest.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record ContractCreateUpdateDto(
        @NotBlank(message = "Имя отсутствует")
        String name,
        List<UUID> historyIds
) {
}

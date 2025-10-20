package ru.springtest.dto;

import jakarta.validation.constraints.NotBlank;

public record ContractCreateUpdateDto(
        @NotBlank(message = "Имя отсутствует")
        String name
) {
}

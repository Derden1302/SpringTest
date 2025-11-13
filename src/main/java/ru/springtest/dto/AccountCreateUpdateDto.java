package ru.springtest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AccountCreateUpdateDto(
        @NotBlank(message = "Имя отсутствует")
        String accountData,
        @NotNull(message = "Айди кастомера отсутствует")
        UUID customerId
){
}

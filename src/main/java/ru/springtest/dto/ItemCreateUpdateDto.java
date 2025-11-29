package ru.springtest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ItemCreateUpdateDto(
        @NotNull(message = "Айди отсутствует")
        UUID sellerId,
        @NotBlank(message = "Имя отсутствует")
        String name
) {
}

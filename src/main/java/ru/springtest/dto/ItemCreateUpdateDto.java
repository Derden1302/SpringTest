package ru.springtest.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ItemCreateUpdateDto(
        @NotBlank(message = "Айди отсутствует")
        UUID sellerId,
        @NotBlank(message = "Имя отсутствует")
        String name
) {
}

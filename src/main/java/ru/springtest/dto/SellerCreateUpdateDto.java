package ru.springtest.dto;

import jakarta.validation.constraints.NotBlank;

public record SellerCreateUpdateDto(
        @NotBlank(message = "Имя отсутствует")
        String name
) {
}

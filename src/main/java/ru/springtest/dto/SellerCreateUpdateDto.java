package ru.springtest.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record SellerCreateUpdateDto(
        @NotBlank(message = "Имя отсутствует")
        String name,
        @NotBlank(message = "Объекты отсутствуют")
        List<ItemDto> item
) {
}

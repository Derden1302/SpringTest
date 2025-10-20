package ru.springtest.dto;

import jakarta.validation.constraints.NotBlank;

public record HistoryCreateUpdateDto(
        @NotBlank(message = "Имя отсутствует")
        String name
){
}

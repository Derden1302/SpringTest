package ru.springtest.dto;

import jakarta.validation.constraints.NotBlank;

public record CustomerCreateUpdateDto(
        @NotBlank(message = "Имя отсутствует")
        String name
){
}

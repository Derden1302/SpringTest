package ru.springtest.dto;

import jakarta.validation.constraints.NotBlank;
import ru.springtest.domain.History;

import java.util.Set;
import java.util.UUID;

public record HistoryCreateUpdateDto(
        @NotBlank(message = "Имя отсутствует")
        String name,
        Set<UUID> contractsIds
){
}

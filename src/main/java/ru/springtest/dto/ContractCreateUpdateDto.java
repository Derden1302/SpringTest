package ru.springtest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import ru.springtest.domain.History;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record ContractCreateUpdateDto(
        @NotBlank(message = "Имя отсутствует")
        String name,
        @NotEmpty(message = "История отсутствует")
        List<@Valid HistoryDto> historyDtos
) {
}
